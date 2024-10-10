-- 사용자 데이터 삽입
INSERT INTO user_tb(username, password, email, created_at) VALUES('길동', '1234', 'a@nate.com', NOW());
INSERT INTO user_tb(username, password, email, created_at) VALUES('둘리', '1234', 'b@nate.com', NOW());
INSERT INTO user_tb(username, password, email, created_at) VALUES('마이콜', '1234', 'c@nate.com', NOW());

-- 게시글 데이터 삽입
INSERT INTO board_tb(title, content, user_id, created_at) VALUES('제목1', '내용1', 1, NOW());
INSERT INTO board_tb(title, content, user_id, created_at) VALUES('제목2', '내용2', 1, NOW());
INSERT INTO board_tb(title, content, user_id, created_at) VALUES('제목3', '내용3', 2, NOW());
INSERT INTO board_tb(title, content, user_id, created_at) VALUES('제목4', '내용4', 3, NOW());
