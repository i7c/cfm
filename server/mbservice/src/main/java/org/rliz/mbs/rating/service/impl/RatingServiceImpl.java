package org.rliz.mbs.rating.service.impl;

import org.rliz.mbs.rating.model.Rated;
import org.rliz.mbs.rating.service.RatingService;
import org.rliz.mbs.recording.model.Recording;
import org.rliz.mbs.release.model.Release;
import org.rliz.mbs.release.model.ReleaseGroup;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implemenation of {@link RatingService}.
 */
@Service
public class RatingServiceImpl implements RatingService {


    private static final String REMOVE_REGEX = "[']+";
    private static final String SPLIT_REGEX = "[\\s!?,:\"«»“”(){}\\[\\]<>^.-]+";

    @Override
    public List<Rated<Recording>> rateRecordings(List<Recording> recordings, String title) {
        String[] titleWords = splitMeaningfulWords(title);

        List<Rated<Recording>> ratedRecordings = recordings.stream()
                .map(recording -> rateRecording(recording, titleWords))
                .collect(Collectors.toList());
        Collections.sort(ratedRecordings);
        return ratedRecordings;
    }

    @Override
    public List<Rated<Release>> rateReleases(List<Release> releases, String title) {
        String[] titleWords = splitMeaningfulWords(title);

        List<Rated<Release>> ratedReleases = releases.stream()
                .map(release -> rateRelease(release, titleWords))
                .collect(Collectors.toList());
        Collections.sort(ratedReleases);
        return ratedReleases;
    }

    @Override
    public List<Rated<ReleaseGroup>> rateReleaseGroups(List<ReleaseGroup> releaseGroups, String title) {
        String[] titleWords = splitMeaningfulWords(title);

        List<Rated<ReleaseGroup>> ratedReleaseGroups = releaseGroups.stream()
                .map(releaseGroup -> rateReleaseGroup(releaseGroup, titleWords))
                .collect(Collectors.toList());
        Collections.sort(ratedReleaseGroups);
        return ratedReleaseGroups;
    }

    /**
     * Rate a single recording.
     *
     * @param recording   a recording
     * @param searchWords search words for the title
     * @return a rated recording
     */
    private Rated<Recording> rateRecording(Recording recording, String[] searchWords) {
        String[] titleWords = splitMeaningfulWords(recording.getName());

        List<String> lcs = longestCommonSubsequence(titleWords, searchWords);
        int rating = (int) ((double) lcs.size() / titleWords.length * 100) + lcs.size();
        return new Rated<>(recording, rating);
    }

    /**
     * Rate a single release.
     *
     * @param release     a release
     * @param searchWords search words for the title
     * @return a rated release
     */
    private Rated<Release> rateRelease(Release release, String[] searchWords) {
        String[] titleWords = splitMeaningfulWords(release.getName());

        List<String> lcs = longestCommonSubsequence(titleWords, searchWords);
        int rating = (int) ((double) lcs.size() / titleWords.length * 100);
        return new Rated<>(release, rating);
    }

    /**
     * Rate a single {@link ReleaseGroup}
     *
     * @param releaseGroup the release group
     * @param searchWords  search words for the desired title
     * @return the rated release group
     */
    private Rated<ReleaseGroup> rateReleaseGroup(ReleaseGroup releaseGroup, String[] searchWords) {
        String[] titleWords = splitMeaningfulWords(releaseGroup.getName());

        List<String> lcs = longestCommonSubsequence(titleWords, searchWords);
        int rating = (int) ((double) lcs.size() / titleWords.length * 100) + lcs.size();
        return new Rated<>(releaseGroup, rating);
    }

    /**
     * Splits string into a set of words removing certain special characters.
     *
     * @param phrase a string with words
     * @return the cleaned set of words of the phrase
     */
    private String[] splitMeaningfulWords(String phrase) {
        return phrase.replaceAll(REMOVE_REGEX, "").toLowerCase().split(SPLIT_REGEX);
    }


    /*
     * Thanks a lot to https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Longest_common_subsequence#Java
     * for this snippet.
     */
    private static <E> List<E> longestCommonSubsequence(E[] s1, E[] s2) {
        int[][] num = new int[s1.length + 1][s2.length + 1];

        for (int i = 1; i <= s1.length; i++)
            for (int j = 1; j <= s2.length; j++)
                if (s1[i - 1].equals(s2[j - 1]))
                    num[i][j] = 1 + num[i - 1][j - 1];
                else
                    num[i][j] = Math.max(num[i - 1][j], num[i][j - 1]);

        int pos1 = s1.length, pos2 = s2.length;
        List<E> result = new LinkedList<E>();

        while (pos1 != 0 && pos2 != 0) {
            if (s1[pos1 - 1].equals(s2[pos2 - 1])) {
                result.add(s1[pos1 - 1]);
                pos1--;
                pos2--;
            } else if (num[pos1][pos2 - 1] >= num[pos1][pos2]) {
                pos2--;
            } else {
                pos1--;
            }
        }
        Collections.reverse(result);
        return result;
    }
}
