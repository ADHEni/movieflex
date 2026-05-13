INSERT INTO genres (genre_id, name) VALUES
(1, 'Sci-Fi'),
(2, 'Drama'),
(3, 'Action');

INSERT INTO actors (actor_id, first_name, last_name) VALUES
(1, 'Keanu', 'Reeves'),
(2, 'Leonardo' , 'DiCaprio'),
(3, 'Song', 'Kang-ho');

INSERT INTO movies (movie_id, title, description, image_url, duration, release_year, rating) VALUES
(1, 'The Matrix', 'A hacker discovers reality is a simulation.', '/images/matrix.jpg', 136, '1999', 8.7),
(2, 'Inception', 'A thief enters dreams to steal secrets.', '/images/inception.jpg', 148, '2010', 8.8),
(3, 'Parasite', 'A poor family infiltrates a wealthy household.', '/images/parasite.jpg', 132, '2019', 8.6);

INSERT INTO movie_genre (movie_id, genre_id) VALUES
(1, 1),
(2, 1),
(3, 2);

INSERT INTO movie_actor (movie_id, actor_id) VALUES
(1, 1),
(2, 2),
(3, 3);