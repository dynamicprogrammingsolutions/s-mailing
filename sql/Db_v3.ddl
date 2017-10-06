ALTER TABLE series_items
  ADD  sendDelayLastItem INT    NOT NULL,
  ADD  sendDelayLastMail INT    NOT NULL,
  ADD  sendOrder         INT    NOT NULL;

ALTER TABLE series_mails
  ADD sentTime              DATETIME NULL;

ALTER TABLE series_subscriptions
  ADD  lastItemOrder    INT      NOT NULL,
  ADD  lastItemSendTime DATETIME NULL;

ALTER TABLE users
  ADD  lastSeriesMailSendTime DATETIME   NULL;
