-- PostgreSQL data initialization
-- Insert test user with properly encoded password (only if not exists)
INSERT INTO users (id, email, password, name, profile_image_url, subscription_tier, created_at, updated_at) 
SELECT '550e8400-e29b-41d4-a716-446655440000', 'testuser@test.com', '$2a$10$2JZuM7tNuBRFbMIdPUbMaOIOWI/wpj20CRf/PmWa.t2JQF/M.Vllm', 'Test User', null, 'FREE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'testuser@test.com');

-- Insert sample habits for testuser@test.com (only if not exists)
INSERT INTO habits (id, user_id, title, description, category, target_frequency, reminder_time, is_active, created_at) 
SELECT '650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000', 'Drink Water', 'Drink 8 glasses of water daily', 'Health & Fitness', 8, '09:00:00'::time, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM habits WHERE id = '650e8400-e29b-41d4-a716-446655440000');

INSERT INTO habits (id, user_id, title, description, category, target_frequency, reminder_time, is_active, created_at) 
SELECT '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440000', 'Morning Exercise', '30 minutes of exercise every morning', 'Health & Fitness', 1, '07:00:00'::time, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM habits WHERE id = '650e8400-e29b-41d4-a716-446655440001');

INSERT INTO habits (id, user_id, title, description, category, target_frequency, reminder_time, is_active, created_at) 
SELECT '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000', 'Read Books', 'Read for 30 minutes daily', 'Learning', 1, '20:00:00'::time, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM habits WHERE id = '650e8400-e29b-41d4-a716-446655440002');

INSERT INTO habits (id, user_id, title, description, category, target_frequency, reminder_time, is_active, created_at) 
SELECT '650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440000', 'Meditation', '10 minutes of mindfulness meditation', 'Mindfulness', 1, '06:30:00'::time, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM habits WHERE id = '650e8400-e29b-41d4-a716-446655440003');

INSERT INTO habits (id, user_id, title, description, category, target_frequency, reminder_time, is_active, created_at) 
SELECT '650e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440000', 'Daily Coding', 'Practice coding for 1 hour', 'Productivity', 1, '14:00:00'::time, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM habits WHERE id = '650e8400-e29b-41d4-a716-446655440004');

INSERT INTO habits (id, user_id, title, description, category, target_frequency, reminder_time, is_active, created_at) 
SELECT '650e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440000', 'Healthy Eating', 'Eat 5 servings of fruits and vegetables', 'Health & Fitness', 5, '12:00:00'::time, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM habits WHERE id = '650e8400-e29b-41d4-a716-446655440005');

INSERT INTO habits (id, user_id, title, description, category, target_frequency, reminder_time, is_active, created_at) 
SELECT '650e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440000', 'Learn Spanish', '15 minutes of Spanish practice', 'Learning', 1, '19:00:00'::time, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM habits WHERE id = '650e8400-e29b-41d4-a716-446655440006');

-- Insert habit completions (only if not exists)
INSERT INTO habit_completions (id, habit_id, completed_at, notes) 
SELECT '750e8400-e29b-41d4-a716-446655440000', '650e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP, 'Finished reading "Atomic Habits" chapter 3'
WHERE NOT EXISTS (SELECT 1 FROM habit_completions WHERE id = '750e8400-e29b-41d4-a716-446655440000');

INSERT INTO habit_completions (id, habit_id, completed_at, notes) 
SELECT '750e8400-e29b-41d4-a716-446655440001', '650e8400-e29b-41d4-a716-446655440004', CURRENT_TIMESTAMP, 'Worked on habit tracking app backend'
WHERE NOT EXISTS (SELECT 1 FROM habit_completions WHERE id = '750e8400-e29b-41d4-a716-446655440001');

INSERT INTO habit_completions (id, habit_id, completed_at, notes) 
SELECT '750e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Completed 8 glasses of water yesterday'
WHERE NOT EXISTS (SELECT 1 FROM habit_completions WHERE id = '750e8400-e29b-41d4-a716-446655440002');

INSERT INTO habit_completions (id, habit_id, completed_at, notes) 
SELECT '750e8400-e29b-41d4-a716-446655440003', '650e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '1 day', '30 min run in the park'
WHERE NOT EXISTS (SELECT 1 FROM habit_completions WHERE id = '750e8400-e29b-41d4-a716-446655440003');

INSERT INTO habit_completions (id, habit_id, completed_at, notes) 
SELECT '750e8400-e29b-41d4-a716-446655440004', '650e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Morning meditation session completed'
WHERE NOT EXISTS (SELECT 1 FROM habit_completions WHERE id = '750e8400-e29b-41d4-a716-446655440004');

INSERT INTO habit_completions (id, habit_id, completed_at, notes) 
SELECT '750e8400-e29b-41d4-a716-446655440005', '650e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Drank water throughout the day'
WHERE NOT EXISTS (SELECT 1 FROM habit_completions WHERE id = '750e8400-e29b-41d4-a716-446655440005');

INSERT INTO habit_completions (id, habit_id, completed_at, notes) 
SELECT '750e8400-e29b-41d4-a716-446655440006', '650e8400-e29b-41d4-a716-446655440005', CURRENT_TIMESTAMP, 'Had a salad for lunch with lots of veggies'
WHERE NOT EXISTS (SELECT 1 FROM habit_completions WHERE id = '750e8400-e29b-41d4-a716-446655440006');

INSERT INTO habit_completions (id, habit_id, completed_at, notes) 
SELECT '750e8400-e29b-41d4-a716-446655440007', '650e8400-e29b-41d4-a716-446655440006', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Completed Duolingo Spanish lesson'
WHERE NOT EXISTS (SELECT 1 FROM habit_completions WHERE id = '750e8400-e29b-41d4-a716-446655440007');

-- Insert AI conversations (only if not exists)
INSERT INTO ai_conversations (id, user_id, message, response, created_at) 
SELECT '850e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000', 'How can I build better habits?', 'Building better habits requires consistency, starting small, and tracking your progress. Focus on one habit at a time and make it as easy as possible to maintain. The key is to start with habits that take less than 2 minutes to complete.', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM ai_conversations WHERE id = '850e8400-e29b-41d4-a716-446655440000');

INSERT INTO ai_conversations (id, user_id, message, response, created_at) 
SELECT '850e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440000', 'What is the best time to exercise?', 'The best time to exercise is when you can be most consistent. Morning workouts often work well because they start your day with energy and are less likely to be disrupted by daily events. However, the most important factor is choosing a time you can stick to regularly.', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM ai_conversations WHERE id = '850e8400-e29b-41d4-a716-446655440001');

INSERT INTO ai_conversations (id, user_id, message, response, created_at) 
SELECT '850e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000', 'I keep forgetting to drink water. Any tips?', 'Here are some effective strategies: 1) Set up visual cues like keeping a water bottle on your desk, 2) Use habit stacking - drink water after existing habits like checking email, 3) Set phone reminders every 2 hours, 4) Track your intake in this app to build awareness. Start with just one extra glass per day.', CURRENT_TIMESTAMP - INTERVAL '1 day'
WHERE NOT EXISTS (SELECT 1 FROM ai_conversations WHERE id = '850e8400-e29b-41d4-a716-446655440002');

INSERT INTO ai_conversations (id, user_id, message, response, created_at) 
SELECT '850e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440000', 'How do I stay motivated when I miss a day?', 'Missing one day is not failure - it''s just data! The key is to get back on track immediately. Use the "never miss twice" rule: if you miss one day, make sure you don''t miss the next day. Focus on progress, not perfection. Your habits are built over months and years, not individual days.', CURRENT_TIMESTAMP - INTERVAL '2 days'
WHERE NOT EXISTS (SELECT 1 FROM ai_conversations WHERE id = '850e8400-e29b-41d4-a716-446655440003');
