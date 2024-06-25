-- liquibase formatted sql
-- changeset author:mvl, dbms:PostgreSQL

-- ПОДПИСКИ ПОЛЬЗОВАТЕЛЕЙ ДРУГ НА ДРУГА
DROP TABLE IF EXISTS public.user_subscriptions;
CREATE TABLE public.user_subscriptions
(
    id bigint PRIMARY KEY,
    subscriber_id bigint NOT NULL REFERENCES public.users(id),
    publisher_id bigint NOT NULL REFERENCES public.users(id),
    valid_from_date date,
    valid_to_date date,
    CHECK (subscriber_id <> publisher_id), -- Пользователь не м.б. подписан сам на себя
    deleted boolean DEFAULT FALSE NOT NULL,
    version bigint
);

CREATE UNIQUE INDEX ix_subscription_subscriber ON public.user_subscriptions (subscriber_id, publisher_id) WHERE deleted = FALSE;
CREATE INDEX ix_subscription_publisher ON public.user_subscriptions (publisher_id) WHERE deleted = FALSE;

COMMENT ON TABLE public.user_subscriptions is 'Подписки пользователей друг на друга.';
COMMENT ON COLUMN public.user_subscriptions.id is 'Первичный ключ.';
COMMENT ON COLUMN public.user_subscriptions.subscriber_id is 'Кто подписан.';
COMMENT ON COLUMN public.user_subscriptions.publisher_id is 'На кого подписан.';
COMMENT ON COLUMN public.user_subscriptions.valid_from_date is 'Дата начала действия подписки.';
COMMENT ON COLUMN public.user_subscriptions.valid_to_date is 'Дата окончания действия подписки.';
COMMENT ON COLUMN public.user_subscriptions.deleted is 'Признак soft delete.';
COMMENT ON COLUMN public.user_subscriptions.version is 'Версия строки. Для оптимистической блокировки.';

-- rollback DROP TABLE IF EXISTS public.user_subscriptions;
