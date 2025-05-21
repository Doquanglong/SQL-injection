import sqlite3

# Connect to (or create) the database file in the same working directory
conn = sqlite3.connect('users.db')
cursor = conn.cursor()

# Drop the users table if it exists (for fresh start)
cursor.execute('DROP TABLE IF EXISTS users')

# Create the users table
cursor.execute('''
    CREATE TABLE users (
        username TEXT,
        password TEXT
    )
''')

# List of users to insert
users = [
    ('admin', 'admin123'),
    ('alice', 'alicepass'),
    ('bob', 'bobsecure'),
    ('charlie', 'charlie321'),
    ('diana', 'diana456'),
]

# Insert users
cursor.executemany('INSERT INTO users (username, password) VALUES (?, ?)', users)

# Commit changes and close connection
conn.commit()
conn.close()

print("users.db created with 5 users.")
