package org.rliz.cfm.musicbrainz.api.dto;

import java.util.List;

/**
 * Represents a list of recordings in the musicbrainz API.
 */
public class MbRecordingListDto {

    private Integer count;

    private Integer offset;

    private List<MbRecordingDto> recordings;

    public Integer getCount() {
        return count;
    }

    public Integer getOffset() {
        return offset;
    }

    public List<MbRecordingDto> getRecordings() {
        return recordings;
    }
}
