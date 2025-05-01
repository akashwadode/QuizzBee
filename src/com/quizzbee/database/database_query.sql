
CREATE DATABASE quiz_db;
USE quiz_db;


CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE questions (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    question_text VARCHAR(255) NOT NULL,
    option_a VARCHAR(100) NOT NULL,
    option_b VARCHAR(100) NOT NULL,
    option_c VARCHAR(100) NOT NULL,
    option_d VARCHAR(100) NOT NULL,
    correct_answer CHAR(1) NOT NULL, -- A, B, C, or D
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);


CREATE TABLE attempts (
    attempt_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    score INT NOT NULL,
    total_questions INT NOT NULL,
    attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);


INSERT INTO categories (category_name) VALUES 
('Maths'), 
('Logical Reasoning'), 
('English'), 
('Computer Science'), 
('AI');

-- Insert questions
-- Maths Questions (category_id = 1)
INSERT INTO questions (category_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(1, 'What is 5 * 3?', '12', '15', '18', '20', 'B'),
(1, 'What is 12 + 7?', '18', '19', '20', '21', 'B'),
(1, 'What is 48 ÷ 6?', '6', '7', '8', '9', 'C'),
(1, 'What is the square of 4?', '12', '14', '16', '18', 'C'),
(1, 'What is 15 - 9?', '5', '6', '7', '8', 'B'),
(1, 'What is 7 * 8?', '54', '56', '58', '60', 'B'),
(1, 'What is 100 ÷ 25?', '3', '4', '5', '6', 'B'),
(1, 'What is 3^3?', '9', '12', '27', '36', 'C'),
(1, 'What is 20% of 50?', '8', '10', '12', '15', 'B'),
(1, 'What is the value of π to two decimals?', '3.12', '3.14', '3.16', '3.18', 'B'),
(1, 'What is 9 + 4 * 2?', '17', '26', '14', '20', 'A'),
(1, 'What is 36 ÷ 9?', '3', '4', '5', '6', 'B'),
(1, 'What is 2 * (3 + 5)?', '10', '16', '18', '20', 'B'),
(1, 'What is the square root of 25?', '4', '5', '6', '7', 'B'),
(1, 'What is 18 - 7?', '9', '10', '11', '12', 'C'),
(1, 'What is 6 * 9?', '52', '54', '56', '58', 'B'),
(1, 'What is 75 ÷ 15?', '4', '5', '6', '7', 'B'),
(1, 'What is 4^2?', '12', '14', '16', '18', 'C'),
(1, 'What is 30% of 40?', '10', '12', '14', '16', 'B'),
(1, 'What is 5 + 3 * 4?', '20', '17', '14', '12', 'B');

-- Logical Reasoning Questions (category_id = 2)
INSERT INTO questions (category_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(2, 'If A is B’s brother and B is C’s sister, who is C to A?', 'Sister', 'Brother', 'Father', 'Mother', 'A'),
(2, 'If today is Wednesday, what day is it in two days?', 'Monday', 'Tuesday', 'Friday', 'Saturday', 'C'),
(2, 'If all cats are mammals and some mammals are dogs, are all cats dogs?', 'Yes', 'No', 'Maybe', 'Sometimes', 'B'),
(2, 'Which number comes next: 2, 4, 6, 8?', '9', '10', '11', '12', 'B'),
(2, 'If X is taller than Y, and Y is taller than Z, who is shortest?', 'X', 'Y', 'Z', 'None', 'C'),
(2, 'What is the odd one out: 3, 6, 9, 12, 14?', '3', '6', '9', '14', 'D'),
(2, 'If 5 books cost $25, how much do 2 books cost?', '$8', '$10', '$12', '$15', 'B'),
(2, 'Which is the next term: 1, 3, 6, 10?', '12', '13', '14', '15', 'D'),
(2, 'If P is Q’s father and Q is R’s daughter, who is R to P?', 'Wife', 'Sister', 'Daughter', 'Mother', 'A'),
(2, 'What comes next: A, C, E, I?', 'J', 'K', 'I', 'L', 'B'),
(2, 'If 3 pens cost $9, how much do 5 pens cost?', '$12', '$15', '$18', '$20', 'B'),
(2, 'Which number does not belong: 4, 8, 12, 15, 16?', '8', '12', '15', '16', 'C'),
(2, 'If Monday is the first day, what is the third day?', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'B'),
(2, 'If 2 apples cost $4, how much do 3 apples cost?', '$5', '$6', '$7', '$8', 'B'),
(2, 'What is the next number: 1, 4, 9, 16?', '20', '22', '25', '28', 'C'),
(2, 'If M is N’s mother and N is O’s sister, who is O to M?', 'Daughter', 'Son', 'Husband', 'Brother', 'A'),
(2, 'Which is different: circle, square, triangle, rectangle?', 'Circle', 'Square', 'Triangle', 'Rectangle', 'A'),
(2, 'If 4 chairs cost $20, how much do 6 chairs cost?', '$25', '$28', '$30', '$32', 'C'),
(2, 'What comes next: 5, 10, 15, 20?', '22', '25', '30', '35', 'B'),
(2, 'If U is V’s brother and V is W’s sister, who is W to U?', 'Sister', 'Brother', 'Father', 'Mother', 'A');

-- English Questions (category_id = 3)
INSERT INTO questions (category_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(3, 'What is a synonym for happy?', 'Sad', 'Joyful', 'Angry', 'Tired', 'B'),
(3, 'What is an antonym for big?', 'Large', 'Small', 'Huge', 'Great', 'B'),
(3, 'Which word is spelled correctly?', 'Recieve', 'Receive', 'Receeve', 'Recive', 'B'),
(3, 'What is the plural of child?', 'Childs', 'Children', 'Childes', 'Childrens', 'B'),
(3, 'What is a synonym for fast?', 'Slow', 'Quick', 'Heavy', 'Calm', 'B'),
(3, 'Choose the correct form: She ___ to the store.', 'Go', 'Goes', 'Going', 'Gone', 'B'),
(3, 'What is an antonym for dark?', 'Light', 'Shadow', 'Black', 'Dim', 'A'),
(3, 'Which is a noun?', 'Run', 'Big', 'House', 'Quickly', 'C'),
(3, 'What is the past tense of sing?', 'Sang', 'Sung', 'Singed', 'Singing', 'A'),
(3, 'What is a synonym for beautiful?', 'Ugly', 'Pretty', 'Plain', 'Simple', 'B'),
(3, 'Choose the correct word: Their house is ___ than ours.', 'Big', 'Bigger', 'Biggest', 'More big', 'B'),
(3, 'What is the plural of mouse?', 'Mouses', 'Mice', 'Mousees', 'Mices', 'B'),
(3, 'What is an antonym for loud?', 'Noisy', 'Quiet', 'Strong', 'Bright', 'B'),
(3, 'Which is an adjective?', 'Table', 'Run', 'Blue', 'Slowly', 'C'),
(3, 'What is the past tense of write?', 'Wrote', 'Written', 'Writing', 'Write', 'A'),
(3, 'What is a synonym for small?', 'Tiny', 'Large', 'Big', 'Huge', 'A'),
(3, 'Choose the correct form: He ___ a book.', 'Read', 'Reads', 'Reading', 'Readed', 'B'),
(3, 'What is the plural of foot?', 'Foots', 'Feet', 'Feets', 'Footes', 'B'),
(3, 'What is an antonym for happy?', 'Sad', 'Joyful', 'Excited', 'Pleased', 'A'),
(3, 'Which is a verb?', 'Car', 'Sing', 'Red', 'Fast', 'B');

-- Computer Science Questions (category_id = 4)
INSERT INTO questions (category_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(4, 'What is a CPU?', 'Central Processing Unit', 'Control Program Unit', 'Computer Power Unit', 'Central Program Utility', 'A'),
(4, 'What does RAM stand for?', 'Read Access Memory', 'Random Access Memory', 'Run Active Memory', 'Rapid Access Module', 'B'),
(4, 'What is the main function of an OS?', 'Run applications', 'Manage hardware', 'Store data', 'Browse internet', 'B'),
(4, 'What is a bit?', 'Binary digit', 'Byte integer', 'Base input', 'Boolean type', 'A'),
(4, 'Which language is used for web development?', 'Python', 'HTML', 'C++', 'Java', 'B'),
(4, 'What does URL stand for?', 'Uniform Resource Locator', 'Universal Reference Link', 'Unique Resource Label', 'Unified Resource Line', 'A'),
(4, 'What is a database?', 'A programming language', 'A storage system', 'A network protocol', 'A hardware component', 'B'),
(4, 'What is the purpose of a compiler?', 'Run programs', 'Translate code', 'Store data', 'Manage memory', 'B'),
(4, 'What does HTTP stand for?', 'HyperText Transfer Protocol', 'High Transfer Text Protocol', 'Hyper Terminal Transport', 'Host Text Protocol', 'A'),
(4, 'What is an IP address?', 'Internet Protocol address', 'Internal Program address', 'Integrated Port address', 'Info Packet address', 'A'),
(4, 'Which is a programming language?', 'SQL', 'HTML', 'CSS', 'XML', 'A'),
(4, 'What is a firewall?', 'A virus', 'A security system', 'A storage device', 'A software bug', 'B'),
(4, 'What does GUI stand for?', 'General Utility Interface', 'Graphical User Interface', 'Global User Input', 'Guided User Interaction', 'B'),
(4, 'What is the binary for 5?', '100', '101', '110', '111', 'B'),
(4, 'What is a loop in programming?', 'A data type', 'A repetitive structure', 'A storage unit', 'A network command', 'B'),
(4, 'What does DNS stand for?', 'Dynamic Network System', 'Domain Name System', 'Digital Number Service', 'Data Network Standard', 'B'),
(4, 'What is a variable?', 'A fixed value', 'A memory location', 'A hardware component', 'A network address', 'B'),
(4, 'What is the purpose of a router?', 'Store data', 'Connect networks', 'Run software', 'Compile code', 'B'),
(4, 'What does SSD stand for?', 'Solid State Drive', 'System Storage Device', 'Secure Data Disk', 'Standard Software Drive', 'A'),
(4, 'What is an algorithm?', 'A programming language', 'A step-by-step solution', 'A hardware component', 'A data type', 'B');

-- AI Questions (category_id = 5)
INSERT INTO questions (category_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(5, 'What does AI stand for?', 'Automated Intelligence', 'Artificial Intelligence', 'Advanced Integration', 'Algorithmic Inference', 'B'),
(5, 'What is machine learning?', 'A programming language', 'A subset of AI', 'A hardware component', 'A database system', 'B'),
(5, 'What is a neural network?', 'A network protocol', 'A model inspired by the brain', 'A storage system', 'A programming tool', 'B'),
(5, 'What is supervised learning?', 'Learning without data', 'Learning with labeled data', 'Learning with hardware', 'Learning without algorithms', 'B'),
(5, 'What is a chatbot?', 'A virus', 'A conversational AI', 'A storage device', 'A programming language', 'B'),
(5, 'What is deep learning?', 'A type of machine learning', 'A network protocol', 'A data storage method', 'A programming paradigm', 'A'),
(5, 'What does NLP stand for?', 'Network Learning Protocol', 'Natural Language Processing', 'Neural Logic Program', 'New Learning Platform', 'B'),
(5, 'What is reinforcement learning?', 'Learning with rewards', 'Learning with labeled data', 'Learning with hardware', 'Learning without data', 'A'),
(5, 'What is a decision tree?', 'A hardware component', 'A machine learning model', 'A network system', 'A data storage', 'B'),
(5, 'What is computer vision?', 'AI for processing images', 'AI for networking', 'AI for data storage', 'AI for programming', 'A'),
(5, 'What is overfitting in AI?', 'Underperforming model', 'Model too specific to data', 'Model with no data', 'Model with hardware issues', 'B'),
(5, 'What is a perceptron?', 'A type of neural network', 'A data storage', 'A network protocol', 'A programming language', 'A'),
(5, 'What does GAN stand for?', 'General Algorithm Network', 'Generative Adversarial Network', 'Global AI Node', 'Guided Analysis Network', 'B'),
(5, 'What is unsupervised learning?', 'Learning with labeled data', 'Learning without labels', 'Learning with hardware', 'Learning with rewards', 'B'),
(5, 'What is a feature in machine learning?', 'A hardware component', 'A data attribute', 'A network protocol', 'A programming tool', 'B'),
(5, 'What is a convolutional neural network used for?', 'Text processing', 'Image processing', 'Data storage', 'Network security', 'B'),
(5, 'What is transfer learning?', 'Transferring hardware', 'Using pre-trained models', 'Transferring data', 'Using new algorithms', 'B'),
(5, 'What is a hyperparameter?', 'A data value', 'A model configuration', 'A hardware setting', 'A network address', 'B'),
(5, 'What is a loss function in AI?', 'A programming error', 'A measure of model error', 'A data storage', 'A network issue', 'B'),
(5, 'What is a recommender system?', 'A hardware component', 'An AI for suggestions', 'A data storage', 'A network protocol', 'B');