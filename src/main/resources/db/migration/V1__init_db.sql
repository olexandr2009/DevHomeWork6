
CREATE TYPE IF NOT EXISTS worker_level AS ENUM
    ('Trainee', 'Junior', 'Middle', 'Senior');

CREATE TABLE IF NOT EXISTS public.clients
(
    client_id IDENTITY NOT NULL,
    client_name character varying(1000) NOT NULL,
    CONSTRAINT client_pkey PRIMARY KEY (client_id),
    CONSTRAINT client_client_name_check CHECK (length(client_name::text) >= 2 AND length(client_name::text) <= 1000)
);

CREATE TABLE IF NOT EXISTS public.projects
(
    project_id IDENTITY NOT NULL,
    client_id BIGINT NOT NULL,
    start_date DATE DEFAULT CURRENT_DATE,
    finish_date DATE,
    CONSTRAINT check_finish_date CHECK (finish_date < CURRENT_DATE),
    CONSTRAINT project_pkey PRIMARY KEY (project_id),
    CONSTRAINT project_client_id_fkey FOREIGN KEY (client_id)
        REFERENCES public.clients (client_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.workers
(
    worker_id IDENTITY NOT NULL,
    birthday date,
    level worker_level,
    salary integer,
    worker_name character varying(1000) NOT NULL,
    CONSTRAINT worker_pkey PRIMARY KEY (worker_id),
    CONSTRAINT worker_name_check CHECK (length(worker_name::text) >= 2 AND length(worker_name::text) <= 1000),
    CONSTRAINT check_birthday CHECK (birthday > '1900-01-01'::date),
    CONSTRAINT check_salary CHECK (salary >= 100 AND salary <= 100000)
);

CREATE table IF NOT EXISTS public.project_workers
(
    project_id BIGINT NOT NULL,
    worker_id BIGINT NOT NULL,
    CONSTRAINT project_worker_pkey PRIMARY KEY (project_id, worker_id),
    CONSTRAINT project_id_fkey FOREIGN KEY (project_id)
        REFERENCES public.projects (project_id) ON DELETE CASCADE,
    CONSTRAINT worker_id_fkey FOREIGN KEY (worker_id)
        REFERENCES public.workers (worker_id)
);
