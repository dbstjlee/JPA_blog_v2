-- 사용자 데이터 삽입
INSERT INTO user_tb(username, password, email, role, created_at) VALUES('길동', '1234', 'a@nate.com', 'USER', NOW());
INSERT INTO user_tb(username, password, email, role, created_at) VALUES('둘리', '1234', 'b@nate.com', 'USER', NOW());
INSERT INTO user_tb(username, password, email, role, created_at) VALUES('마이콜', '1234', 'c@nate.com', 'ADMIN', NOW());

-- 게시글 데이터 삽입
INSERT INTO board_tb(title, content, user_id, created_at) VALUES('제목1', '내용1', 1, NOW());
INSERT INTO board_tb(title, content, user_id, created_at) VALUES('제목2', '내용2', 1, NOW());
INSERT INTO board_tb(title, content, user_id, created_at) VALUES('제목3', '내용3', 2, NOW());
INSERT INTO board_tb(title, content, user_id, created_at) VALUES('제목4', '내용4', 3, NOW());

-- 댓글 데이터 삽입
INSERT INTO reply_tb(comment, board_id, user_id, created_at, status) VALUES('댓글1', 4, 1, NOW(), 'DELETED');
INSERT INTO reply_tb(comment, board_id, user_id, created_at, status) VALUES('댓글1', 4, 1, NOW(), 'ACTIVE');
INSERT INTO reply_tb(comment, board_id, user_id, created_at, status) VALUES('댓글2', 4, 1, NOW(), 'DELETED');
INSERT INTO reply_tb(comment, board_id, user_id, created_at, status) VALUES('댓글3', 4, 2, NOW(), 'ACTIVE');
INSERT INTO reply_tb(comment, board_id, user_id, created_at, status) VALUES('댓글4', 3, 2, NOW(), 'ACTIVE');