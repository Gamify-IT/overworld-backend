--liquibase formatted sql

--changeset 13on4rd:202504161
-- delete existing achievement related tables
DROP TABLE IF EXISTS "achievement" CASCADE;
DROP TABLE IF EXISTS "achievement_categories" CASCADE;
DROP TABLE IF EXISTS "achievement_statistic" CASCADE;
DROP TABLE IF EXISTS "achievement_statistic_interacted_objects" CASCADE;
DROP TABLE IF EXISTS "player_achievement_statistics" CASCADE;

--changeset 13on4rd:202504162
-- create achievement related tables with new structure
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE "achievement"
(
    "id"                UUID    NOT NULL,
    "achievement_title" INTEGER NOT NULL,
    "amount_required"   INTEGER NOT NULL,
    "description"       VARCHAR(255),
    "image_name"        VARCHAR(255),
    "course_id"         INTEGER,
    CONSTRAINT "achievement_pkey" PRIMARY KEY ("id")
);
ALTER TABLE "achievement"
    ADD CONSTRAINT "fk12orhai85gjq544il8s2jsbdl" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE CASCADE;

CREATE TABLE "achievement_categories"
(
    "achievement_id" UUID NOT NULL,
    "categories"     INTEGER
);
ALTER TABLE "achievement_categories"
    ADD CONSTRAINT "fkb6aptbv8q314wndmnioet6v8e" FOREIGN KEY ("achievement_id") REFERENCES "achievement" ("id") ON UPDATE NO ACTION ON DELETE CASCADE;

CREATE TABLE "achievement_statistic"
(
    "id"             UUID         NOT NULL,
    "completed"      BOOLEAN      NOT NULL,
    "progress"       INTEGER      NOT NULL,
    "achievement_id" UUID         NOT NULL,
    "player_user_id" VARCHAR(255) NOT NULL,
    "course_id"      INTEGER      NOT NULL,
    CONSTRAINT "achievement_statistic_pkey" PRIMARY KEY ("id")
);
ALTER TABLE "achievement_statistic"
    ADD CONSTRAINT "uksnsaeurvphh56fvpbsdribsf5" UNIQUE ("player_user_id", "course_id", "achievement_id");
ALTER TABLE "achievement_statistic"
    ADD CONSTRAINT "fkd21dk5l8wkghjv3s20d4btc8x" FOREIGN KEY ("achievement_id") REFERENCES "achievement" ("id") ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE "achievement_statistic"
    ADD CONSTRAINT "fk14q4mqkxir3s28xk1mw4sviep" FOREIGN KEY ("player_user_id") REFERENCES "player" ("user_id") ON UPDATE NO ACTION ON DELETE CASCADE;

CREATE TABLE "player_achievement_statistics"
(
    "player_user_id"            VARCHAR(255) NOT NULL,
    "achievement_statistics_id" UUID         NOT NULL
);
ALTER TABLE "player_achievement_statistics"
    ADD CONSTRAINT "uksa1a2g6ghhuf4p6g6xwqf6p1l" UNIQUE ("achievement_statistics_id", "player_user_id");
ALTER TABLE "player_achievement_statistics"
    ADD CONSTRAINT "fknedx1yjlpvg4j57xkqefl1878" FOREIGN KEY ("player_user_id") REFERENCES "player" ("user_id") ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE "player_achievement_statistics"
    ADD CONSTRAINT "fkmtiaq4kijok214irvrna9po4k" FOREIGN KEY ("achievement_statistics_id") REFERENCES "achievement_statistic" ("id") ON UPDATE NO ACTION ON DELETE CASCADE;

CREATE TABLE "achievement_statistic_interacted_objects"
(
    "achievement_statistic_id" UUID NOT NULL,
    "dungeon_id"               INTEGER,
    "number_id"                INTEGER,
    "world_id"                 INTEGER
);
ALTER TABLE "achievement_statistic_interacted_objects"
    ADD CONSTRAINT "fkmtiaq4kijok63ghrvrna9po4k" FOREIGN KEY ("achievement_statistic_id") REFERENCES "achievement_statistic" ("id") ON UPDATE NO ACTION ON DELETE CASCADE;

-- changeset 13on4rd:202504163
-- update player-course table
CREATE TABLE "player_course"
(
    "player_user_id" VARCHAR(255) NOT NULL,
    "course_id"      INTEGER      NOT NULL,
    CONSTRAINT "player_course_pl" PRIMARY KEY ("player_user_id", "course_id")
);
ALTER TABLE "player_course"
    ADD CONSTRAINT "ukp08e5ji56gd4sgtwe7olekjax" UNIQUE ("player_user_id", "course_id");
ALTER TABLE "player_course"
    ADD CONSTRAINT "fk92jj8uhlftzsvl90b57vs9gbb" FOREIGN KEY ("player_user_id") REFERENCES "player" ("user_id") ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE "player_course"
    ADD CONSTRAINT "fk12orn67j6fgvcw4il8s2jsbdl" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE CASCADE;

-- changeset 13on4rd:202504164
-- create default achievements for each existing course
INSERT INTO achievement (id, achievement_title, amount_required, description, image_name, course_id)
SELECT gen_random_uuid(),
       0,
       10,
       'Walk 10 tiles',
       'footImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       1,
       1000,
       'Walk 1000 tiles',
       'footImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       2,
       1,
       'Change skin of your character',
       'npcImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       4,
       30,
       'Play for 30 minutes',
       'clockImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       5,
       90,
       'Play for 90 minutes',
       'clockImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       6,
       30,
       'Use sprint for 30 seconds',
       'rocketImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       20,
       4,
       'Open 4 teleporters',
       'teleporterImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       21,
       10,
       'Open 10 teleporters',
       'teleporterImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       34,
       3,
       'Use UFO 3 times',
       'ufoImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       35,
       2,
       'Login for 2 days',
       'calenderImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       36,
       5,
       'Login for 5 days',
       'calenderImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       37,
       1,
       'Take first place in the leaderboard',
       'medal1Image',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       38,
       1,
       'Enter 2d or 3d place in the leaderboard',
       'medal3Image',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       39,
       50,
       'Get 50 coins',
       'coinImage',
       course.id
FROM course
UNION ALL
SELECT gen_random_uuid(),
       40,
       150,
       'Get 150 coins',
       'coinImage',
       course.id
FROM course;