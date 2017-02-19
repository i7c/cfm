-- Name: ix_acn_acp; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_acn_acp ON artist_credit_name USING btree (artist_credit, "position");
-- Name: ix_acn_artist; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_acn_artist ON artist_credit_name USING btree (artist);
-- Name: ix_acn_artistcredit; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_acn_artistcredit ON artist_credit_name USING btree (artist_credit);
-- Name: ix_artist_gid; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_artist_gid ON artist USING btree (gid);
-- Name: ix_artist_id; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_artist_id ON artist USING btree (id);
-- Name: ix_artist_name; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_artist_name ON artist USING btree (name);
-- Name: ix_artistcredit_id; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_artistcredit_id ON artist_credit USING btree (id);
-- Name: ix_medium_id; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_medium_id ON medium USING btree (id);
-- Name: ix_medium_release; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_medium_release ON medium USING btree (release);
-- Name: ix_recording_artistcredit; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_recording_artistcredit ON recording USING btree (artist_credit);
-- Name: ix_recording_gid; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_recording_gid ON recording USING btree (gid);
-- Name: ix_recording_id; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_recording_id ON recording USING btree (id);
-- Name: ix_release_artistcredit; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_release_artistcredit ON release USING btree (artist_credit);
-- Name: ix_release_id; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_release_id ON release USING btree (id);
-- Name: ix_release_rg; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_release_rg ON release USING btree (release_group);
-- Name: ix_releasegroup_id; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_releasegroup_id ON release_group USING btree (id);
-- Name: ix_track_id; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_track_id ON track USING btree (id);
-- Name: ix_track_medium; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_track_medium ON track USING btree (medium);
-- Name: ix_track_recording; Type: INDEX; Schema: musicbrainz; Owner: mbdb
CREATE INDEX ix_track_recording ON track USING btree (recording);
