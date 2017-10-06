
CREATE TABLE `Campaign` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `longName` varchar(256) NOT NULL
);

CREATE TABLE `Campaign_mails` (
  `campaigns_id` int(11) NOT NULL,
  `mails_id` int(11) NOT NULL
);

CREATE TABLE `Campaign_users` (
  `unsubscribedFromCampaigns_id` int(11) NOT NULL,
  `unsubscribedUsers_id` int(11) NOT NULL
);

CREATE TABLE `generated_mails` (
  `id` int(11) NOT NULL,
  `fromEmail` varchar(64) NOT NULL,
  `toEmail` varchar(64) NOT NULL,
  `subject` varchar(128) NOT NULL,
  `body` text NOT NULL
);


CREATE TABLE `mails` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `subject` varchar(128) NOT NULL,
  `fromEmail` varchar(128) NOT NULL,
  `body_text` text
);

CREATE TABLE `mail_queue` (
  `id` int(11) NOT NULL,
  `mail_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `generated_mail_id` int(11) DEFAULT NULL,
  `scheduledTime` datetime DEFAULT NULL,
  `sentTime` datetime DEFAULT NULL,
  `status` varchar(16) NOT NULL DEFAULT 'unsent'
);

CREATE TABLE `series` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `displayName` varchar(128) DEFAULT NULL,
  `updateSubscribeTime` tinyint(4) NOT NULL DEFAULT '0'
);

CREATE TABLE `series_items` (
  `id` int(11) NOT NULL,
  `series_id` int(11) NOT NULL,
  `mail_id` int(11) NOT NULL,
  `sendDelay` int(11) NOT NULL
);

CREATE TABLE `series_mails` (
  `id` int(11) NOT NULL,
  `seriesSubscription_id` int(11) NOT NULL,
  `seriesItem_id` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0'
);

CREATE TABLE `series_subscriptions` (
  `id` int(11) NOT NULL,
  `series_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `subscribeTime` datetime NOT NULL,
  `extraData` text
);

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(64) NOT NULL,
  `firstname` varchar(64) NOT NULL,
  `lastname` varchar(64) NOT NULL,
  `status` varchar(16) NOT NULL DEFAULT '0'
);

ALTER TABLE `Campaign`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `Campaign_mails`
  ADD PRIMARY KEY (`campaigns_id`,`mails_id`);

ALTER TABLE `Campaign_users`
  ADD PRIMARY KEY (`unsubscribedFromCampaigns_id`,`unsubscribedUsers_id`);

ALTER TABLE `generated_mails`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `mails`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `mail_queue`
  ADD PRIMARY KEY (`id`),
  ADD KEY `mail_id` (`mail_id`,`user_id`,`generated_mail_id`);

ALTER TABLE `series`
  ADD PRIMARY KEY (`id`),
  ADD KEY `name` (`name`);

ALTER TABLE `series_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `seriesId` (`series_id`,`mail_id`);

ALTER TABLE `series_mails`
  ADD PRIMARY KEY (`id`),
  ADD KEY `seriesSubscription_id` (`seriesSubscription_id`,`seriesItem_id`);

ALTER TABLE `series_subscriptions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `series_id` (`series_id`,`user_id`);

ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `email` (`email`),
  ADD KEY `status` (`status`);

ALTER TABLE `Campaign`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `generated_mails`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `mails`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `mail_queue`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `series`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `series_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `series_mails`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `series_subscriptions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
