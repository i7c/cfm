--
-- PostgreSQL database dump
--

-- Dumped from database version 10.1
-- Dumped by pg_dump version 10.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: cfm_user; Type: TABLE; Schema: public; Owner: cfmuser
--

CREATE TABLE cfm_user (
    oid bigint NOT NULL,
    uuid uuid NOT NULL,
    name character varying(128) NOT NULL,
    password character varying(128) NOT NULL,
    state character varying(255),
    system_user boolean NOT NULL
);


ALTER TABLE cfm_user OWNER TO cfmuser;

--
-- Name: fingerprint; Type: TABLE; Schema: public; Owner: cfmuser
--

CREATE TABLE fingerprint (
    oid bigint NOT NULL,
    uuid uuid NOT NULL,
    fingerprint character varying(128),
    recording_uuid uuid,
    release_group_uuid uuid,
    user_oid bigint NOT NULL
);


ALTER TABLE fingerprint OWNER TO cfmuser;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: cfmuser
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO cfmuser;

--
-- Name: now_playing; Type: TABLE; Schema: public; Owner: cfmuser
--

CREATE TABLE now_playing (
    oid bigint NOT NULL,
    recording_title character varying(255) NOT NULL,
    recording_uuid uuid,
    release_group_uuid uuid,
    release_title character varying(255) NOT NULL,
    "timestamp" bigint NOT NULL,
    user_oid bigint NOT NULL
);


ALTER TABLE now_playing OWNER TO cfmuser;

--
-- Name: now_playing_artists; Type: TABLE; Schema: public; Owner: cfmuser
--

CREATE TABLE now_playing_artists (
    now_playing_oid bigint NOT NULL,
    artists character varying(255)
);


ALTER TABLE now_playing_artists OWNER TO cfmuser;

--
-- Name: playback; Type: TABLE; Schema: public; Owner: cfmuser
--

CREATE TABLE playback (
    oid bigint NOT NULL,
    uuid uuid NOT NULL,
    fix_attempt bigint,
    play_time bigint,
    recording_uuid uuid,
    release_group_uuid uuid,
    source character varying(255),
    "timestamp" bigint NOT NULL,
    original_data_oid bigint,
    user_oid bigint NOT NULL
);


ALTER TABLE playback OWNER TO cfmuser;

--
-- Name: raw_playback_data; Type: TABLE; Schema: public; Owner: cfmuser
--

CREATE TABLE raw_playback_data (
    oid bigint NOT NULL,
    artist_json character varying(1024) NOT NULL,
    disc_number integer,
    length bigint,
    recording_title character varying(255) NOT NULL,
    release_title character varying(255) NOT NULL,
    track_number integer
);


ALTER TABLE raw_playback_data OWNER TO cfmuser;

--
-- Name: raw_playback_data_artists; Type: TABLE; Schema: public; Owner: cfmuser
--

CREATE TABLE raw_playback_data_artists (
    raw_playback_data_oid bigint NOT NULL,
    artists character varying(255)
);


ALTER TABLE raw_playback_data_artists OWNER TO cfmuser;

--
-- Name: cfm_user cfm_user_pkey; Type: CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY cfm_user
    ADD CONSTRAINT cfm_user_pkey PRIMARY KEY (oid);


--
-- Name: fingerprint fingerprint_pkey; Type: CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY fingerprint
    ADD CONSTRAINT fingerprint_pkey PRIMARY KEY (oid);


--
-- Name: now_playing ix_nowplaying_user; Type: CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY now_playing
    ADD CONSTRAINT ix_nowplaying_user UNIQUE (user_oid);


--
-- Name: playback ix_playback_user_uuid; Type: CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY playback
    ADD CONSTRAINT ix_playback_user_uuid UNIQUE (user_oid, uuid);


--
-- Name: cfm_user ix_user_uuid; Type: CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY cfm_user
    ADD CONSTRAINT ix_user_uuid UNIQUE (uuid);


--
-- Name: now_playing now_playing_pkey; Type: CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY now_playing
    ADD CONSTRAINT now_playing_pkey PRIMARY KEY (oid);


--
-- Name: playback playback_pkey; Type: CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY playback
    ADD CONSTRAINT playback_pkey PRIMARY KEY (oid);


--
-- Name: raw_playback_data raw_playback_data_pkey; Type: CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY raw_playback_data
    ADD CONSTRAINT raw_playback_data_pkey PRIMARY KEY (oid);


--
-- Name: cfm_user uc_user_name; Type: CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY cfm_user
    ADD CONSTRAINT uc_user_name UNIQUE (name);


--
-- Name: playback uk_ghbcxr0wq3ssxkjk19li49uib; Type: CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY playback
    ADD CONSTRAINT uk_ghbcxr0wq3ssxkjk19li49uib UNIQUE (original_data_oid);


--
-- Name: ix_fingerprint_fingerprint; Type: INDEX; Schema: public; Owner: cfmuser
--

CREATE INDEX ix_fingerprint_fingerprint ON fingerprint USING btree (fingerprint);


--
-- Name: ix_fingerprint_user_fingerprint; Type: INDEX; Schema: public; Owner: cfmuser
--

CREATE INDEX ix_fingerprint_user_fingerprint ON fingerprint USING btree (user_oid, fingerprint);


--
-- Name: ix_playback_recordinguuid; Type: INDEX; Schema: public; Owner: cfmuser
--

CREATE INDEX ix_playback_recordinguuid ON playback USING btree (recording_uuid);


--
-- Name: ix_playback_releasegroupuuid; Type: INDEX; Schema: public; Owner: cfmuser
--

CREATE INDEX ix_playback_releasegroupuuid ON playback USING btree (release_group_uuid);


--
-- Name: ix_playback_user; Type: INDEX; Schema: public; Owner: cfmuser
--

CREATE INDEX ix_playback_user ON playback USING btree (user_oid);


--
-- Name: ix_playback_userandsource; Type: INDEX; Schema: public; Owner: cfmuser
--

CREATE INDEX ix_playback_userandsource ON playback USING btree (user_oid, source);


--
-- Name: fingerprint fk_fingerprint_user; Type: FK CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY fingerprint
    ADD CONSTRAINT fk_fingerprint_user FOREIGN KEY (user_oid) REFERENCES cfm_user(oid);


--
-- Name: now_playing fk_nowplaying_user; Type: FK CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY now_playing
    ADD CONSTRAINT fk_nowplaying_user FOREIGN KEY (user_oid) REFERENCES cfm_user(oid);


--
-- Name: playback fk_playback_originaldata; Type: FK CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY playback
    ADD CONSTRAINT fk_playback_originaldata FOREIGN KEY (original_data_oid) REFERENCES raw_playback_data(oid);


--
-- Name: playback fk_playback_user; Type: FK CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY playback
    ADD CONSTRAINT fk_playback_user FOREIGN KEY (user_oid) REFERENCES cfm_user(oid);


--
-- Name: now_playing_artists fkojolqkfrcdh026gtcknf01emh; Type: FK CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY now_playing_artists
    ADD CONSTRAINT fkojolqkfrcdh026gtcknf01emh FOREIGN KEY (now_playing_oid) REFERENCES now_playing(oid);


--
-- Name: raw_playback_data_artists fkxwoarxas32auxt7le63ltc1o; Type: FK CONSTRAINT; Schema: public; Owner: cfmuser
--

ALTER TABLE ONLY raw_playback_data_artists
    ADD CONSTRAINT fkxwoarxas32auxt7le63ltc1o FOREIGN KEY (raw_playback_data_oid) REFERENCES raw_playback_data(oid);


--
-- PostgreSQL database dump complete
--

