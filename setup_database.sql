use quiz_app;

create table if not exists emailotp (
    id char(26) primary key,
    email varchar(255) not null,
    otp int not null,
    createdat timestamp default current_timestamp
);

create table if not exists users (
    id char(26) primary key,
    username text,
    email text not null,
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp
);

create table if not exists contacts (
    id char(26) primary key,
    userid char(26) not null,
    relateduserid char(26),
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp,
    foreign key (userid) references users(id),
    foreign key (relateduserid) references users(id),
    index userid_index (userid),
    index relateduserid_index (relateduserid)
);

create table if not exists quiztemplate (
    id char(26) primary key,
    userid char(26) not null,
    quiztemplatetitle varchar(255) not null,
    quiztemplate longtext not null,
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp,
    foreign key (userid) references users(id),
    index userid_index (userid)
);

create table if not exists quizzes (
    id char(26) primary key,
    userid char(26) not null,
    quiztitle text not null,
    quizdata longtext not null,
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp,
    expiration timestamp,
    foreign key (userid) references users(id),
    index userid_index (userid)
);

create table if not exists submissions (
    id char(26) primary key,
    userid char(26) not null,
    quizid char(26) not null,
    quizanswers longtext not null,
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp,
    foreign key (userid) references users(id),
    foreign key (quizid) references quizzes(id),
    index userid_index (userid),
    index quizid_index (quizid)
);

create table if not exists invites (
    id char(26) primary key,
    userid char(26) not null,
    quizid char(26) not null,
    invitestatus boolean not null,
    createdat timestamp default current_timestamp,
    updatedat timestamp on update current_timestamp,
    foreign key (userid) references users(id),
    foreign key (quizid) references quizzes(id),
    index userid_index (userid),
    index quizid_index (quizid)
);