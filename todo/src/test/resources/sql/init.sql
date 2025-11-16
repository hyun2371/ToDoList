create database todolist;

INSERT INTO todolist.user (user_id, created_at, updated_at, email, nickname, password, role) VALUES (1, '2025-11-16 21:41:23.000000', '2025-11-16 21:41:22.000000', 'user1@gmail.com', 'user1', 'user1234', 'NORMAL');
INSERT INTO todolist.user (user_id, created_at, updated_at, email, nickname, password, role) VALUES (2, '2025-11-16 23:00:34.000000', '2025-11-16 23:00:35.000000', 'user2@gmail.com', 'user2', 'user12345', 'NORMAL');
INSERT INTO todolist.user (user_id, created_at, updated_at, email, nickname, password, role) VALUES (3, '2025-11-16 23:30:14.000000', '2025-11-16 23:30:15.000000', 'user3@gmail.com', 'user3', 'user123456', 'NORMAL');


INSERT INTO todolist.todo (todo_id, created_at, updated_at, category, content, due_date, is_completed, is_deleted, order_index, user_id) VALUES (1, null, '2025-11-16 21:50:43.668689', 'STUDY', '할일1', '2025-11-16', false, false, 1, 1);
INSERT INTO todolist.todo (todo_id, created_at, updated_at, category, content, due_date, is_completed, is_deleted, order_index, user_id) VALUES (2, null, null, 'STUDY', '할일2', '2025-11-16', false, false, 2, 1);
INSERT INTO todolist.todo (todo_id, created_at, updated_at, category, content, due_date, is_completed, is_deleted, order_index, user_id) VALUES (3, '2025-11-16 21:49:19.101765', '2025-11-16 21:53:15.362112', 'STUDY', '할일3', '2025-11-18', false, false, 3, 1);
INSERT INTO todolist.todo (todo_id, created_at, updated_at, category, content, due_date, is_completed, is_deleted, order_index, user_id) VALUES (4, '2025-11-16 21:49:42.372300', '2025-11-16 22:16:58.240966', 'STUDY', '할일4', '2025-11-16', false, false, 4, 1);
INSERT INTO todolist.todo (todo_id, created_at, updated_at, category, content, due_date, is_completed, is_deleted, order_index, user_id) VALUES (5, '2025-11-16 22:35:13.997937', '2025-11-16 22:35:13.997937', 'STUDY', '할일5', '2025-11-16', false, false, 5, 1);

INSERT INTO todolist.share (share_id, created_at, updated_at, owner_id, viewer_id) VALUES (2, '2025-11-16 23:30:43.954402', '2025-11-16 23:30:43.954402', 2, 3);