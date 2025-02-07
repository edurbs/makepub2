CREATE TABLE public.bible
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    version character varying NOT NULL,
    book character varying NOT NULL,
    chapter character varying NOT NULL,
    verse character varying NOT NULL,
    text character varying,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.bible
    OWNER to postgres;