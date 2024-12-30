create table if not exists emailotp (
    id varchar(40) primary key,
    email varchar(255) not null,
    otp int not null,
    createdat timestamp default current_timestamp
);

create table if not exists users (
    id varchar(40) primary key,
    username text,
    email text not null,
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp
);

create table if not exists quiztemplate (
    id varchar(40) primary key,
    userid varchar(40) not null,
    quiztemplatetitle varchar(255) not null,
    quiztemplate longtext not null,
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp
);

create table if not exists submissions (
    id varchar(40) primary key,
    userid varchar(40) not null,
    quizid varchar(40) not null,
    quizanswers longtext not null,
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp
);

create table if not exists quizzes (
    id varchar(40) primary key,
    userid varchar(40) not null,
    quiztitle text not null,
    quizdata longtext not null,
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp
);

create table if not exists invites (
    id varchar(40) primary key,
    userid varchar(40) not null,
    quizid varchar(40) not null,
    invitestatus boolean not null,
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp
);

create table if not exists contacts (
    id varchar(40) primary key,
    userid varchar(40) not null,
    relateduserid varchar(40),
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp
);