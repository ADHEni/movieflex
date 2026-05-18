
INSERT INTO genres (name) VALUES
('Sci-Fi'),
('Drama'),
('Action');

INSERT INTO actors (first_name, last_name) VALUES
('Keanu', 'Reeves'),
('Leonardo' , 'DiCaprio'),
('Song', 'Kang-ho');

INSERT INTO movies (title, description, image_url, duration, release_year, rating) VALUES
('The Matrix', 'A hacker discovers reality is a simulation.', '/images/matrix.jpg', 136, '1999'),
('Inception', 'A thief enters dreams to steal secrets.', '/images/inception.jpg', 148, '2010'),
('Parasite', 'A poor family infiltrates a wealthy household.', '/images/parasite.jpg', 132, '2019');

INSERT INTO movie_genre (movie_id, genre_id) VALUES
(1, 1),
(2, 1),
(3, 2);

INSERT INTO movie_actor (movie_id, actor_id) VALUES
(1, 1),
(2, 2),
(3, 3);