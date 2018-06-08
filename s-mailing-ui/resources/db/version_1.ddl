create table Campaign (id  bigserial not null, longName varchar(255) not null, name varchar(255) not null, primary key (id))
create table Campaign_mails (campaigns_id int8 not null, mails_id int8 not null, primary key (campaigns_id, mails_id))
create table Campaign_users (unsubscribedFromCampaigns_id int8 not null, unsubscribedUsers_id int8 not null, primary key (unsubscribedFromCampaigns_id, unsubscribedUsers_id))
create table generated_mails (id  bigserial not null, body text, fromEmail varchar(255) not null, subject varchar(255) not null, toEmail varchar(255) not null, primary key (id))
create table mail_queue (id  bigserial not null, scheduledTime timestamp, sentTime timestamp, status int4 not null, generated_mail_id int8, mail_id int8 not null, user_id int8 not null, primary key (id))
create table mails (id  bigserial not null, body_text text not null, fromEmail varchar(255) not null, name varchar(255) not null, subject varchar(255) not null, primary key (id))
create table series (id  bigserial not null, displayName varchar(255) not null, name varchar(255) not null, updateSubscribeTime boolean, primary key (id))
create table series_items (id  bigserial not null, sendDelay int4 not null, sendDelayLastItem int4 not null, sendDelayLastMail int4 not null, sendOrder int4 not null, mail_id int8, series_id int8, primary key (id))
create table series_mails (sentTime timestamp, status int4, seriesItem_id int8 not null, seriesSubscription_id int8 not null, primary key (seriesItem_id, seriesSubscription_id))
create table series_subscriptions (id  bigserial not null, extraData text, lastItemOrder int4 not null, lastItemSendTime timestamp, subscribeTime timestamp, series_id int8, user_id int8, primary key (id))
create table users (id  bigserial not null, email varchar(255) not null, firstName varchar(255) not null, lastName varchar(255) not null, lastSeriesMailSendTime timestamp, status int4 not null, primary key (id))
alter table if exists Campaign_mails add constraint FKom1lw2r1sm52xb1j4op3ps95e foreign key (mails_id) references mails
alter table if exists Campaign_mails add constraint FK1pqi7a94wyb95qo5ce8b6gjuv foreign key (campaigns_id) references Campaign
alter table if exists Campaign_users add constraint FKrny5wscebxiofk80le0bierae foreign key (unsubscribedUsers_id) references users
alter table if exists Campaign_users add constraint FKmw4mf17f0ayxxsxur161cmjvp foreign key (unsubscribedFromCampaigns_id) references Campaign
alter table if exists mail_queue add constraint FKiusn5igdvgb9vmie0ggsf4d7v foreign key (generated_mail_id) references generated_mails
alter table if exists mail_queue add constraint FKinu2r84pgpq68iq3y0vtvq05w foreign key (mail_id) references mails
alter table if exists mail_queue add constraint FKslgr0f4kudn66cighuxxi4p00 foreign key (user_id) references users
alter table if exists series_items add constraint FKjt8y52txwbpv8idnytba8in2m foreign key (mail_id) references mails
alter table if exists series_items add constraint FKqcw1tl53rhv7ovg527dnpy1ps foreign key (series_id) references series
alter table if exists series_mails add constraint FK7hhsap2n86u43kjw4267i5r48 foreign key (seriesItem_id) references series_items
alter table if exists series_mails add constraint FKi311xt9g4g0wka3x9n529xael foreign key (seriesSubscription_id) references series_subscriptions
alter table if exists series_subscriptions add constraint FKr43u5c7g149v1thelcnqrksw3 foreign key (series_id) references series
alter table if exists series_subscriptions add constraint FK2npv19txgsgkdj1cs3xay9rc1 foreign key (user_id) references users
