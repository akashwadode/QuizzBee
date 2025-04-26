

Database Notes
users: Simplified to user_id, username, password (plain text), and created_at.
categories: Updated to Maths, Logical Reasoning, English, Computer Science, AI.
questions: Includes one sample question per category.
attempts: Tracks quiz attempts for leaderboard calculations.
No leaderboard table: Leaderboard is derived from attempts using a SQL query.