-- liquibase formatted sql

-- changeset leon:change1-1
CREATE TABLE "player_task_statistic" ("id" UUID NOT NULL, "completed" BOOLEAN NOT NULL, "highscore" BIGINT NOT NULL, "course_id" INTEGER, "minigame_task_id" UUID, "player_statistic_id" UUID, CONSTRAINT "player_task_statistic_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-2
CREATE TABLE "course" ("id" INTEGER NOT NULL, "active" BOOLEAN NOT NULL, "course_name" VARCHAR(255) NOT NULL, "description" VARCHAR(255), "semester" VARCHAR(255), CONSTRAINT "course_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-3
CREATE TABLE "book" ("id" UUID NOT NULL, "description" VARCHAR(255), "index" INTEGER NOT NULL, "text" VARCHAR(1000000), "area_id" UUID, "course_id" INTEGER, CONSTRAINT "book_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-4
CREATE TABLE "player_statistic_playernpcstatistics" ("player_statistic_id" UUID NOT NULL, "playernpcstatistics_id" UUID NOT NULL, CONSTRAINT "player_statistic_playernpcstatistics_pkey" PRIMARY KEY ("player_statistic_id", "playernpcstatistics_id"));

-- changeset leon:change1-5
CREATE TABLE "playernpcstatistic_playernpcaction_logs" ("playernpcstatistic_id" UUID NOT NULL, "playernpcaction_logs_id" UUID NOT NULL, CONSTRAINT "playernpcstatistic_playernpcaction_logs_pkey" PRIMARY KEY ("playernpcstatistic_id", "playernpcaction_logs_id"));

-- changeset leon:change1-6
CREATE TABLE "course_worlds" ("course_id" INTEGER NOT NULL, "worlds_id" UUID NOT NULL);

-- changeset leon:change1-7
CREATE TABLE "area_npcs" ("area_id" UUID NOT NULL, "npcs_id" UUID NOT NULL, CONSTRAINT "area_npcs_pkey" PRIMARY KEY ("area_id", "npcs_id"));

-- changeset leon:change1-8
CREATE TABLE "area_dungeons" ("world_id" UUID NOT NULL, "dungeons_id" UUID NOT NULL);

-- changeset leon:change1-9
CREATE TABLE "course_player_statistics" ("course_id" INTEGER NOT NULL, "player_statistics_id" UUID NOT NULL, CONSTRAINT "course_player_statistics_pkey" PRIMARY KEY ("course_id", "player_statistics_id"));

-- changeset leon:change1-10
CREATE TABLE "player_statistic_player_task_statistics" ("player_statistic_id" UUID NOT NULL, "player_task_statistics_id" UUID NOT NULL, CONSTRAINT "player_statistic_player_task_statistics_pkey" PRIMARY KEY ("player_statistic_id", "player_task_statistics_id"));

-- changeset leon:change1-11
CREATE TABLE "area_books" ("area_id" UUID NOT NULL, "books_id" UUID NOT NULL, CONSTRAINT "area_books_pkey" PRIMARY KEY ("area_id", "books_id"));

-- changeset leon:change1-12
CREATE TABLE "area_minigame_tasks" ("area_id" UUID NOT NULL, "minigame_tasks_id" UUID NOT NULL, CONSTRAINT "area_minigame_tasks_pkey" PRIMARY KEY ("area_id", "minigame_tasks_id"));

-- changeset leon:change1-13
CREATE TABLE "player_task_statistic_player_task_action_logs" ("player_task_statistic_id" UUID NOT NULL, "player_task_action_logs_id" UUID NOT NULL, CONSTRAINT "player_task_statistic_player_task_action_logs_pkey" PRIMARY KEY ("player_task_statistic_id", "player_task_action_logs_id"));

-- changeset leon:change1-14
CREATE TABLE "player_achievement_statistics" ("player_user_id" VARCHAR(255) NOT NULL, "achievement_statistics_id" UUID NOT NULL);

-- changeset leon:change1-15
CREATE TABLE "minigame_task" ("id" UUID NOT NULL, "configuration_id" UUID, "description" VARCHAR(255), "game" VARCHAR(255), "index" INTEGER NOT NULL, "area_id" UUID, "course_id" INTEGER, CONSTRAINT "minigame_task_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-16
CREATE TABLE "npc" ("id" UUID NOT NULL, "description" VARCHAR(255), "index" INTEGER NOT NULL, "area_id" UUID, "course_id" INTEGER, CONSTRAINT "npc_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-17
CREATE TABLE "achievement_statistic" ("id" UUID NOT NULL, "completed" BOOLEAN NOT NULL, "progress" INTEGER NOT NULL, "achievement_achievement_title" INTEGER, "player_user_id" VARCHAR(255), CONSTRAINT "achievement_statistic_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-18
ALTER TABLE "course" ADD CONSTRAINT "uk820tkd4yvetwbn59037xp1ix0" UNIQUE ("course_name", "semester");

-- changeset leon:change1-19
ALTER TABLE "book" ADD CONSTRAINT "uk9unjth2xglyrweh7ythk0ehc7" UNIQUE ("index", "area_id", "course_id");

-- changeset leon:change1-20
ALTER TABLE "player_statistic_playernpcstatistics" ADD CONSTRAINT "uk_7w9c0idi7mg84wh2fv0mjwes" UNIQUE ("playernpcstatistics_id");

-- changeset leon:change1-21
ALTER TABLE "playernpcstatistic_playernpcaction_logs" ADD CONSTRAINT "uk_8neeqyks7p4oqru9c0j04x5fi" UNIQUE ("playernpcaction_logs_id");

-- changeset leon:change1-22
ALTER TABLE "course_worlds" ADD CONSTRAINT "uk_8wb9mpt836gtib5lkw17yyiy9" UNIQUE ("worlds_id");

-- changeset leon:change1-23
ALTER TABLE "area_npcs" ADD CONSTRAINT "uk_af9lyjvml2klp6f7j2salyajs" UNIQUE ("npcs_id");

-- changeset leon:change1-24
ALTER TABLE "area_dungeons" ADD CONSTRAINT "uk_arxrussoxrq8qul4udrv80kv6" UNIQUE ("dungeons_id");

-- changeset leon:change1-25
ALTER TABLE "course_player_statistics" ADD CONSTRAINT "uk_ay0oo04jui0f401m0t26c784s" UNIQUE ("player_statistics_id");

-- changeset leon:change1-26
ALTER TABLE "player_statistic_player_task_statistics" ADD CONSTRAINT "uk_b47dqi2chebh46j4lhs6dc0y1" UNIQUE ("player_task_statistics_id");

-- changeset leon:change1-27
ALTER TABLE "area_books" ADD CONSTRAINT "uk_ccmryae7idl5i7ei17jsd76bh" UNIQUE ("books_id");

-- changeset leon:change1-28
ALTER TABLE "area_minigame_tasks" ADD CONSTRAINT "uk_dcdrgvmm9tleh9ayt0wo72tj" UNIQUE ("minigame_tasks_id");

-- changeset leon:change1-29
ALTER TABLE "player_task_statistic_player_task_action_logs" ADD CONSTRAINT "uk_l0gdwtngrk6s19rmp9r9mj7s" UNIQUE ("player_task_action_logs_id");

-- changeset leon:change1-30
ALTER TABLE "player_achievement_statistics" ADD CONSTRAINT "uk_sa1a2f6ghhuf4p6g6xwqf6p1l" UNIQUE ("achievement_statistics_id");

-- changeset leon:change1-31
ALTER TABLE "minigame_task" ADD CONSTRAINT "ukp08e5jhus5o7aafkk7olekjwh" UNIQUE ("index", "area_id", "course_id");

-- changeset leon:change1-32
ALTER TABLE "npc" ADD CONSTRAINT "ukrp1s2edjwhvyjjmj9m4wlapfg" UNIQUE ("index", "area_id", "course_id");

-- changeset leon:change1-33
ALTER TABLE "achievement_statistic" ADD CONSTRAINT "uksnsaeurvphh55fvpbsdribsf5" UNIQUE ("player_user_id", "achievement_achievement_title");

-- changeset leon:change1-34
CREATE SEQUENCE  IF NOT EXISTS "hibernate_sequence" AS bigint START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

-- changeset leon:change1-35
CREATE TABLE "achievement" ("achievement_title" INTEGER NOT NULL, "amount_required" INTEGER NOT NULL, "description" VARCHAR(255), "image_name" VARCHAR(255), CONSTRAINT "achievement_pkey" PRIMARY KEY ("achievement_title"));

-- changeset leon:change1-36
CREATE TABLE "achievement_categories" ("achievement_achievement_title" INTEGER NOT NULL, "categories" INTEGER);

-- changeset leon:change1-37
CREATE TABLE "area" ("dtype" VARCHAR(31) NOT NULL, "id" UUID NOT NULL, "active" BOOLEAN NOT NULL, "configured" BOOLEAN NOT NULL, "index" INTEGER NOT NULL, "static_name" VARCHAR(255) NOT NULL, "topic_name" VARCHAR(255), "course_id" INTEGER, "world_id" UUID, CONSTRAINT "area_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-38
CREATE TABLE "npc_text" ("npc_id" UUID NOT NULL, "text" VARCHAR(255));

-- changeset leon:change1-39
CREATE TABLE "player" ("user_id" VARCHAR(255) NOT NULL, "username" VARCHAR(255), CONSTRAINT "player_pkey" PRIMARY KEY ("user_id"));

-- changeset leon:change1-40
CREATE TABLE "player_statistic" ("id" UUID NOT NULL, "knowledge" BIGINT NOT NULL, "user_id" VARCHAR(255) NOT NULL, "username" VARCHAR(255) NOT NULL, "course_id" INTEGER, "current_area_id" UUID, CONSTRAINT "player_statistic_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-41
CREATE TABLE "player_statistic_completed_dungeons" ("player_statistic_id" UUID NOT NULL, "completed_dungeons_id" UUID NOT NULL);

-- changeset leon:change1-42
CREATE TABLE "player_statistic_unlocked_areas" ("player_statistic_id" UUID NOT NULL, "unlocked_areas_id" UUID NOT NULL);

-- changeset leon:change1-43
CREATE TABLE "player_statistic_unlocked_teleporters" ("player_statistic_id" UUID NOT NULL, "unlocked_teleporters_id" UUID NOT NULL, CONSTRAINT "player_statistic_unlocked_teleporters_pkey" PRIMARY KEY ("player_statistic_id", "unlocked_teleporters_id"));

-- changeset leon:change1-44
CREATE TABLE "player_task_action_log" ("id" UUID NOT NULL, "configuration_id" UUID, "current_highscore" BIGINT NOT NULL, "date" TIMESTAMP WITHOUT TIME ZONE, "gained_knowledge" BIGINT NOT NULL, "game" VARCHAR(255), "score" BIGINT NOT NULL, "course_id" INTEGER, "player_task_statistic_id" UUID, CONSTRAINT "player_task_action_log_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-45
CREATE TABLE "playernpcaction_log" ("id" UUID NOT NULL, "date" TIMESTAMP WITHOUT TIME ZONE, "gained_knowledge" BIGINT NOT NULL, "course_id" INTEGER, "playernpcstatistic_id" UUID, CONSTRAINT "playernpcaction_log_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-46
CREATE TABLE "playernpcstatistic" ("id" UUID NOT NULL, "completed" BOOLEAN NOT NULL, "course_id" INTEGER, "npc_id" UUID, "player_statistic_id" UUID, CONSTRAINT "playernpcstatistic_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-47
CREATE TABLE "teleporter" ("id" UUID NOT NULL, "index" INTEGER NOT NULL, "area_id" UUID, "course_id" INTEGER, CONSTRAINT "teleporter_pkey" PRIMARY KEY ("id"));

-- changeset leon:change1-48
ALTER TABLE "player_task_statistic" ADD CONSTRAINT "fk12orm06caiu19w8il8s2jsbdl" FOREIGN KEY ("minigame_task_id") REFERENCES "minigame_task" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-49
ALTER TABLE "achievement_statistic" ADD CONSTRAINT "fk14q4mqixir3s28xk1mw4sviep" FOREIGN KEY ("player_user_id") REFERENCES "player" ("user_id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-50
ALTER TABLE "playernpcaction_log" ADD CONSTRAINT "fk1f7o7i4v9w82cxrg0u14tgmym" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-51
ALTER TABLE "player_task_statistic" ADD CONSTRAINT "fk1okhn876e2a6uulje8e3fuuk5" FOREIGN KEY ("player_statistic_id") REFERENCES "player_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-52
ALTER TABLE "npc" ADD CONSTRAINT "fk2t7w14eq7c78bfpcoufe8eyne" FOREIGN KEY ("area_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-53
ALTER TABLE "area_books" ADD CONSTRAINT "fk33aovkdu8oaajb819ir107s1o" FOREIGN KEY ("books_id") REFERENCES "book" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-54
ALTER TABLE "minigame_task" ADD CONSTRAINT "fk42f1f9nt23vs2nl5hu90ol2a3" FOREIGN KEY ("area_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-55
ALTER TABLE "area_dungeons" ADD CONSTRAINT "fk4g1gtrb4sbw3lejfd2uh6tyw8" FOREIGN KEY ("dungeons_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-56
ALTER TABLE "player_statistic_playernpcstatistics" ADD CONSTRAINT "fk4jq025mftyeyep71t98trqf3l" FOREIGN KEY ("player_statistic_id") REFERENCES "player_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-57
ALTER TABLE "teleporter" ADD CONSTRAINT "fk4ldxb6bx1el2gn9e7mhnj4fwq" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-58
ALTER TABLE "playernpcstatistic" ADD CONSTRAINT "fk58fhj6qipi15slyct040a2mt6" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-59
ALTER TABLE "playernpcstatistic" ADD CONSTRAINT "fk59ejtn8fulc26ymdqt5fb1mrr" FOREIGN KEY ("npc_id") REFERENCES "npc" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-60
ALTER TABLE "course_player_statistics" ADD CONSTRAINT "fk5y3ghl2hplxka6o877v7r6rsp" FOREIGN KEY ("player_statistics_id") REFERENCES "player_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-61
ALTER TABLE "player_task_statistic_player_task_action_logs" ADD CONSTRAINT "fk66lg03286gi0s62k5sg46dc90" FOREIGN KEY ("player_task_action_logs_id") REFERENCES "player_task_action_log" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-62
ALTER TABLE "area_minigame_tasks" ADD CONSTRAINT "fk6os2w0w1v3euv2k1ngj30nhdr" FOREIGN KEY ("minigame_tasks_id") REFERENCES "minigame_task" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-63
ALTER TABLE "playernpcstatistic_playernpcaction_logs" ADD CONSTRAINT "fk6r4drao88j3upygr9uj2d9187" FOREIGN KEY ("playernpcstatistic_id") REFERENCES "playernpcstatistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-64
ALTER TABLE "player_task_action_log" ADD CONSTRAINT "fk6uf4h7kq387mei941oweb32pl" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-65
ALTER TABLE "playernpcstatistic" ADD CONSTRAINT "fk92jj8ujawdasvl90b57vs9gbb" FOREIGN KEY ("player_statistic_id") REFERENCES "player_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-66
ALTER TABLE "area_minigame_tasks" ADD CONSTRAINT "fk9urdfdsvqwbb3buy0dcdxws6k" FOREIGN KEY ("area_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-67
ALTER TABLE "player_task_statistic" ADD CONSTRAINT "fk9y1c39d5bbnw5sh3oh3nm7wum" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-68
ALTER TABLE "achievement_categories" ADD CONSTRAINT "fkb6aptbv8q317wndmnioet6v8e" FOREIGN KEY ("achievement_achievement_title") REFERENCES "achievement" ("achievement_title") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-69
ALTER TABLE "player_statistic_completed_dungeons" ADD CONSTRAINT "fkb8e7inkwlcg3gk8tbymhsq6ul" FOREIGN KEY ("completed_dungeons_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-70
ALTER TABLE "player_statistic_completed_dungeons" ADD CONSTRAINT "fkbj119ecx4k8i5chsv8dv7fdfg" FOREIGN KEY ("player_statistic_id") REFERENCES "player_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-71
ALTER TABLE "player_statistic_unlocked_areas" ADD CONSTRAINT "fkc8rmgmwe16v3mm33jsy9texyr" FOREIGN KEY ("unlocked_areas_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-72
ALTER TABLE "achievement_statistic" ADD CONSTRAINT "fkd21dk5l8wjghjv3s20d4btc8x" FOREIGN KEY ("achievement_achievement_title") REFERENCES "achievement" ("achievement_title") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-73
ALTER TABLE "player_task_action_log" ADD CONSTRAINT "fkdlfw5fmcsknwuql63b86b45a1" FOREIGN KEY ("player_task_statistic_id") REFERENCES "player_task_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-74
ALTER TABLE "teleporter" ADD CONSTRAINT "fkf2jorprpskqigvstk23ifqimn" FOREIGN KEY ("area_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-75
ALTER TABLE "player_statistic" ADD CONSTRAINT "fki1rwj9je6dm6avyhqh7lj7gjt" FOREIGN KEY ("current_area_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-76
ALTER TABLE "player_statistic_player_task_statistics" ADD CONSTRAINT "fki7g5w4ig8hyiaqqjab7apf5xr" FOREIGN KEY ("player_task_statistics_id") REFERENCES "player_task_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-77
ALTER TABLE "player_statistic_player_task_statistics" ADD CONSTRAINT "fkiv8joyn2x1r38uf5lhsminya8" FOREIGN KEY ("player_statistic_id") REFERENCES "player_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-78
ALTER TABLE "player_statistic" ADD CONSTRAINT "fkivnttptny2j311jg06hmgsmbt" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-79
ALTER TABLE "player_statistic_unlocked_teleporters" ADD CONSTRAINT "fkjdbputaywsxhotgho1cxnx7v3" FOREIGN KEY ("player_statistic_id") REFERENCES "player_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-80
ALTER TABLE "minigame_task" ADD CONSTRAINT "fkjj5msugs6tsg0dpkyhg5rg3fj" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-81
ALTER TABLE "player_task_statistic_player_task_action_logs" ADD CONSTRAINT "fkjm1bdvgey2qg1lfngx2abi0sm" FOREIGN KEY ("player_task_statistic_id") REFERENCES "player_task_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-82
ALTER TABLE "area_npcs" ADD CONSTRAINT "fkjrt31eq7amlbhumjf62fhexia" FOREIGN KEY ("area_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-83
ALTER TABLE "playernpcstatistic_playernpcaction_logs" ADD CONSTRAINT "fkjx0ula9op9ne4mub427p552m" FOREIGN KEY ("playernpcaction_logs_id") REFERENCES "playernpcaction_log" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-84
ALTER TABLE "area" ADD CONSTRAINT "fkke3rmfk70r7ot54sy24vw442q" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-85
ALTER TABLE "npc_text" ADD CONSTRAINT "fkkgia5fha5jshcattp7t0iaelm" FOREIGN KEY ("npc_id") REFERENCES "npc" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-86
ALTER TABLE "player_statistic_playernpcstatistics" ADD CONSTRAINT "fkkprti8utwbb5d6uug4cs63h9w" FOREIGN KEY ("playernpcstatistics_id") REFERENCES "playernpcstatistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-87
ALTER TABLE "npc" ADD CONSTRAINT "fkl4iy0bskn9q6bxtbbxfcydnj8" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-88
ALTER TABLE "playernpcaction_log" ADD CONSTRAINT "fkly1wr6ylc85twrxav2nhtnimd" FOREIGN KEY ("playernpcstatistic_id") REFERENCES "playernpcstatistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-89
ALTER TABLE "player_achievement_statistics" ADD CONSTRAINT "fkmtiaq4kijok224irvrna9po4k" FOREIGN KEY ("achievement_statistics_id") REFERENCES "achievement_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-90
ALTER TABLE "area_books" ADD CONSTRAINT "fkn0nlgq66g162qd395kgeinpyb" FOREIGN KEY ("area_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-91
ALTER TABLE "area_dungeons" ADD CONSTRAINT "fkn1r4adt88177xhsnx2jiripue" FOREIGN KEY ("world_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-92
ALTER TABLE "course_worlds" ADD CONSTRAINT "fknedx1yjlpvg4j57xkqefl1888" FOREIGN KEY ("worlds_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-93
ALTER TABLE "player_achievement_statistics" ADD CONSTRAINT "fko991s7ct02ttu5mlctu21gplr" FOREIGN KEY ("player_user_id") REFERENCES "player" ("user_id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-94
ALTER TABLE "course_player_statistics" ADD CONSTRAINT "fkoklc3m3jittssm0fnyy97s6gq" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-95
ALTER TABLE "area" ADD CONSTRAINT "fkp3yyg6h1oplrq0upvdw5acekg" FOREIGN KEY ("world_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-96
ALTER TABLE "book" ADD CONSTRAINT "fkpbtrb5t9s5tlmmv7x86kmvkgj" FOREIGN KEY ("area_id") REFERENCES "area" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-97
ALTER TABLE "player_statistic_unlocked_areas" ADD CONSTRAINT "fkqkahskoqcupdnaf0moeg6mi5e" FOREIGN KEY ("player_statistic_id") REFERENCES "player_statistic" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-98
ALTER TABLE "area_npcs" ADD CONSTRAINT "fkqthws1lsesl8bgsq477p8gv4m" FOREIGN KEY ("npcs_id") REFERENCES "npc" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-99
ALTER TABLE "course_worlds" ADD CONSTRAINT "fkr7o1p1hsb0ngdf36ouqchb00w" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-100
ALTER TABLE "book" ADD CONSTRAINT "fkrow3o1i3tcpejmaggbpv1d4fr" FOREIGN KEY ("course_id") REFERENCES "course" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:change1-101
ALTER TABLE "player_statistic_unlocked_teleporters" ADD CONSTRAINT "fktpvtctd86u2sbre1bij25qpjw" FOREIGN KEY ("unlocked_teleporters_id") REFERENCES "teleporter" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset leon:unique-constraints-for-statistics-1
ALTER TABLE "playernpcstatistic" ADD CONSTRAINT "uk04l2vwuyj3u1gde7oanv" UNIQUE ("player_statistic_id", "npc_id", "course_id");

-- changeset leon:unique-constraints-for-statistics-2
ALTER TABLE "player_task_statistic" ADD CONSTRAINT "uk27n56n1liyicdgpg3lzo" UNIQUE ("player_statistic_id", "minigame_task_id", "course_id");

