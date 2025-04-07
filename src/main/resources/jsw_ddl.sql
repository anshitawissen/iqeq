-- document_statuses Table

CREATE TABLE IF NOT EXISTS public.document_status
(
    document_status_id bigint NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default" NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    is_archive boolean NOT NULL,
    revision_no integer NOT NULL,
    updated_by character varying(255) COLLATE pg_catalog."default",
    updated_date timestamp(6) without time zone,
    color_code character varying(255) COLLATE pg_catalog."default",
    icon_name character varying(255) COLLATE pg_catalog."default",
    label character varying(255) COLLATE pg_catalog."default" NOT NULL,
    ui_sequence integer,
    value character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT document_status_pkey PRIMARY KEY (document_status_id),
    CONSTRAINT uk_hp6okdbjq61g6jljy351h9vsy UNIQUE (label)
)

--document_status_mapping

CREATE TABLE IF NOT EXISTS public.document_status_mapping
(
    document_id bigint NOT NULL,
    document_status_id bigint NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default" NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    is_archive boolean NOT NULL,
    revision_no integer NOT NULL,
    updated_by character varying(255) COLLATE pg_catalog."default",
    updated_date timestamp(6) without time zone,
    assigned_at timestamp(6) without time zone NOT NULL,
    CONSTRAINT document_status_mapping_pkey PRIMARY KEY (document_id, document_status_id),
    CONSTRAINT fkp7ywejc1v6me8oncqq1kkpil3 FOREIGN KEY (document_status_id)
        REFERENCES public.document_status (document_status_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fktj4jjf6cn1ugs504772s4jx6s FOREIGN KEY (document_id)
        REFERENCES public.document (document_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
