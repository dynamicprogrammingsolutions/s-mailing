ALTER TABLE Campaign
  MODIFY id BIGINT AUTO_INCREMENT;

ALTER TABLE Campaign_mails
  MODIFY campaigns_id BIGINT NOT NULL,
  MODIFY mails_id BIGINT NOT NULL;

CREATE INDEX FKom1lw2r1sm52xb1j4op3ps95e ON Campaign_mails (mails_id);

ALTER TABLE Campaign_users
  MODIFY unsubscribedFromCampaigns_id BIGINT NOT NULL,
  MODIFY unsubscribedUsers_id         BIGINT NOT NULL;

CREATE INDEX FKrny5wscebxiofk80le0bierae
  ON Campaign_users (unsubscribedUsers_id);

ALTER TABLE generated_mails
  MODIFY id        BIGINT AUTO_INCREMENT,
  MODIFY body      LONGTEXT     NULL,
  MODIFY fromEmail VARCHAR(255) NOT NULL,
  MODIFY subject   VARCHAR(255) NOT NULL,
  MODIFY toEmail   VARCHAR(255) NOT NULL;

ALTER TABLE mail_queue
  MODIFY  id                BIGINT AUTO_INCREMENT,
  MODIFY  scheduledTime     DATETIME NULL,
  MODIFY  sentTime          DATETIME NULL,
  MODIFY  status            INT      NOT NULL,
  MODIFY  generated_mail_id BIGINT   NULL,
  MODIFY  mail_id           BIGINT   NOT NULL,
  MODIFY  user_id           BIGINT   NOT NULL;

CREATE INDEX FKinu2r84pgpq68iq3y0vtvq05w
  ON mail_queue (mail_id);
CREATE INDEX FKiusn5igdvgb9vmie0ggsf4d7v
  ON mail_queue (generated_mail_id);
CREATE INDEX FKslgr0f4kudn66cighuxxi4p00
  ON mail_queue (user_id);

ALTER TABLE mails
  MODIFY  id        BIGINT AUTO_INCREMENT,
  MODIFY  body_text LONGTEXT     NOT NULL,
  MODIFY  fromEmail VARCHAR(255) NOT NULL,
  MODIFY  name      VARCHAR(255) NOT NULL,
  MODIFY  subject   VARCHAR(255) NOT NULL;

ALTER TABLE series
  MODIFY  id                  BIGINT AUTO_INCREMENT,
  MODIFY  displayName         VARCHAR(255) NOT NULL,
  MODIFY  name                VARCHAR(255) NOT NULL,
  MODIFY  updateSubscribeTime BIT          NULL;

ALTER TABLE series_items
  MODIFY  id        BIGINT AUTO_INCREMENT,
  MODIFY  sendDelay INT    NOT NULL,
  MODIFY  mail_id   BIGINT NULL,
  MODIFY  series_id BIGINT NULL;

CREATE INDEX FKjt8y52txwbpv8idnytba8in2m
  ON series_items (mail_id);
CREATE INDEX FKqcw1tl53rhv7ovg527dnpy1ps
  ON series_items (series_id);

ALTER TABLE series_mails
  MODIFY  status                INT    NULL,
  MODIFY  seriesItem_id         BIGINT NOT NULL,
  MODIFY  seriesSubscription_id BIGINT NOT NULL;

CREATE INDEX FKi311xt9g4g0wka3x9n529xael
  ON series_mails (seriesSubscription_id);

ALTER TABLE series_subscriptions
  MODIFY  id            BIGINT AUTO_INCREMENT,
  MODIFY  extraData     LONGTEXT NULL,
  MODIFY  subscribeTime DATETIME NULL,
  MODIFY  series_id     BIGINT   NULL,
  MODIFY  user_id       BIGINT   NULL;

CREATE INDEX FK2npv19txgsgkdj1cs3xay9rc1
  ON series_subscriptions (user_id);
CREATE INDEX FKr43u5c7g149v1thelcnqrksw3
  ON series_subscriptions (series_id);

ALTER TABLE users
  MODIFY  id        BIGINT AUTO_INCREMENT,
  MODIFY  email     VARCHAR(255) NOT NULL,
  MODIFY  firstName VARCHAR(255) NOT NULL,
  MODIFY  lastName  VARCHAR(255) NOT NULL,
  MODIFY  status    INT          NOT NULL;


DROP INDEX mail_id ON mail_queue;
DROP INDEX seriesId ON series_items;
DROP INDEX seriesSubscription_id ON series_mails;
DROP INDEX series_id ON series_subscriptions;
DROP INDEX name ON series;
DROP INDEX email ON users;
DROP INDEX status ON users;
