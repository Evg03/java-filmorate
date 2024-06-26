DROP TABLE IF EXISTS mpa, films, users, friends, likes, genres, film_genres CASCADE;

CREATE table mpa (
  id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(300)
);

CREATE table films  (
  id integer GENERATED BY DEFAULT AS identity PRIMARY KEY,
  name varchar(300) NOT NULL,
  description varchar(200),
  release_date date NOT NULL,
  duration integer NOT NULL,
  mpa_id integer NOT null references mpa(id) on delete cascade
);

CREATE table users (
  id integer GENERATED BY DEFAULT AS identity PRIMARY KEY,
  name varchar(300) NOT NULL,
  email varchar(300) NOT NULL,
  login varchar(300) UNIQUE NOT NULL,
  birthday date NOT NULL
);

CREATE table friends (
  user_id integer references users(id) on delete cascade,
  subscribed_to integer references users(id) on delete cascade,
  PRIMARY KEY (user_id, subscribed_to)
);

CREATE table likes (
  film_id integer references films(id) on delete cascade,
  user_id integer references users(id) on delete cascade,
  PRIMARY KEY (film_id, user_id)
);

CREATE table genres (
  id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(300) NOT NULL
);

CREATE table film_genres (
  film_id integer references films(id) on delete cascade,
  genre_id integer references genres(id) on delete cascade,
  PRIMARY KEY (film_id, genre_id)
);