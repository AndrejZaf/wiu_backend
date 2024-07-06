-- Delete post
INSERT INTO post (id, title, content, author_id, tags, search_vector, created_date)
VALUES ('a8bf75cc-5d78-4f42-9387-0dd3be6bc454', 'Delete Post Title 1', 'Delete Test Post Content 1', '1a9b6303-656c-4e88-9ec6-cb31fe34c7f2', null, 'TEst test testing v1 v2 v3', CURRENT_DATE);

--  Invalid ownership
INSERT INTO post (id, title, content, author_id, tags, search_vector, created_date)
VALUES ('d05e777d-537d-4973-98fd-4504322c7d21', 'Delete Post Title 2', 'Delete Test Post Content 2', '74e19a20-cfd1-46ed-9434-d9319e2e8845', null, 'TEst test testing v1 v2 v3', CURRENT_DATE);

-- Read post
INSERT INTO post (id, title, content, author_id, tags, search_vector, created_date)
VALUES ('f74ebd6f-eba9-4dca-9859-72ed8acedd97', 'Test Post Title 1', 'Test Post Content 1', '1a9b6303-656c-4e88-9ec6-cb31fe34c7f2', null, 'read1', CURRENT_DATE);

-- Update post
INSERT INTO post (id, title, content, author_id, tags, search_vector, created_date)
VALUES ('d40abe07-2fc3-4905-839a-be4f0f0a1ae8', 'Test Post Title 2', 'Test Post Content 1', '1a9b6303-656c-4e88-9ec6-cb31fe34c7f2', null, 'TEst test testing v1 v2 v3', CURRENT_DATE);
