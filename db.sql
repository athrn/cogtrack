
-- TODO: Reconsider Tags usage. How, when, why

Create Table GameConfigKey
(
id Integer Primary Key,
name Text Not Null Unique,
prettyName Text Not Null,
description Text Not Null
);

-- Create Index GameConfigKeyNameIndex On GameConfigKey(name) Asc

-- Description
Insert Into GameConfigKey (name, prettyName, description)
Values ('type', 'Type', 'Type of game');

Insert Into GameConfigKey (name, prettyName, description)
Values ('nback', 'N-Back', 'Match if current character matches N steps back');

Insert Into GameConfigKey (name, prettyName, description)
Values ('delayTime', 'Delay Time', 'Delay between characters (seconds)');

Insert Into GameConfigKey (name, prettyName, description)
Values ('visibleTime', 'Visible Time', 'Time to show character (seconds)');

Insert Into GameConfigKey (name, prettyName, description)
Values ('rounds', 'Number of Rounds', 'Number of rounds per session');

Insert Into GameConfigKey (name, prettyName, description)
Values ('numMatches', 'Number of Matches', 'Number of matches per session');

Insert Into GameConfigKey (name, prettyName, description)
Values ('characterSet', 'Character Set', 'Characters to choose from');

-- Internationalization. Move prettyName and description to separate table with language column and inner join with with ProgramConfig.language

Create Table Game
(
id Integer Primary Key,
name Text Not Null Unique
);

Create Table GameConfig
(
gameId Integer References Game(id),
gameConfigKeyId Integer References GameConfigKey(id),
"value" Text Not Null
);

Create Table GameSession
(
id Integer Primary Key,
timestamp Real Default (julianday('now')),
gameId Integer References Game(id)
);


-------
-- Score
-------
-- TODO: Store everything as reals??? Makes it hard to filter on rounds > 10

Create Table ScoreKey
(
id Integer Primary Key,
name Text Not Null Unique,
prettyName Text Not Null,
description Text Not Null
);

Insert Into ScoreKey (name, prettyName, description)
Values ('type', 'Type', 'Type of game');

Create Table Score
(
gameSessionId Integer References GameSession(id),
scoreKeyId Integer References ScoreKey(id),
value Real Not Null
);

-------
-- Tags
-------
Create Table Tag
(
id Integer Primary Key,
name Text Not Null
);

Create Table GameSessionTags
(
gameSessionId Integer References GameSession(id),
tagId Integer References Tag(id)
);

--------------------
-- Program Stuff
--------------------

-- Keep track of when updates happen if there are changes in scoring etc.
Create Table ProgramUpdates
(
timestamp Real Default (julianday('now')),
version Text Not Null
);

Insert Into ProgramUpdates (version) Values ('0.1');
