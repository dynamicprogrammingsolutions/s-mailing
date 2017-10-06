ALTER TABLE `Campaign_mails`
  ADD CONSTRAINT `FK1pqi7a94wyb95qo5ce8b6gjuv` FOREIGN KEY (`campaigns_id`) REFERENCES `Campaign` (`id`),
  ADD CONSTRAINT `FKom1lw2r1sm52xb1j4op3ps95e` FOREIGN KEY (`mails_id`) REFERENCES `mails` (`id`);

ALTER TABLE `Campaign_users`
  ADD CONSTRAINT `FKmw4mf17f0ayxxsxur161cmjvp` FOREIGN KEY (`unsubscribedFromCampaigns_id`) REFERENCES `Campaign` (`id`),
  ADD CONSTRAINT `FKrny5wscebxiofk80le0bierae` FOREIGN KEY (`unsubscribedUsers_id`) REFERENCES `users` (`id`);

ALTER TABLE `mail_queue`
  ADD CONSTRAINT `FKslgr0f4kudn66cighuxxi4p00` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKinu2r84pgpq68iq3y0vtvq05w` FOREIGN KEY (`mail_id`) REFERENCES `mails` (`id`),
  ADD CONSTRAINT `FKiusn5igdvgb9vmie0ggsf4d7v` FOREIGN KEY (`generated_mail_id`) REFERENCES `generated_mails` (`id`);

ALTER TABLE `series_items`
  ADD CONSTRAINT `FKqcw1tl53rhv7ovg527dnpy1ps` FOREIGN KEY (`series_id`) REFERENCES `series` (`id`),
  ADD CONSTRAINT `FKjt8y52txwbpv8idnytba8in2m` FOREIGN KEY (`mail_id`) REFERENCES `mails` (`id`);

ALTER TABLE `series_mails`
  ADD CONSTRAINT `FKi311xt9g4g0wka3x9n529xael` FOREIGN KEY (`seriesSubscription_id`) REFERENCES `series_subscriptions` (`id`),
  ADD CONSTRAINT `FK7hhsap2n86u43kjw4267i5r48` FOREIGN KEY (`seriesItem_id`) REFERENCES `series_items` (`id`);

ALTER TABLE `series_subscriptions`
  ADD CONSTRAINT `FK2npv19txgsgkdj1cs3xay9rc1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKr43u5c7g149v1thelcnqrksw3` FOREIGN KEY (`series_id`) REFERENCES `series` (`id`);
