-- SmartPrep schema generated from the local Hibernate-created MySQL database.
-- MySQL 8.0 or newer is required for this collation and UUID_TO_BIN in seed.sql.

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `Submissions`;
DROP TABLE IF EXISTS `Test_Cases`;
DROP TABLE IF EXISTS `Proficiencies`;
DROP TABLE IF EXISTS `Problems`;
DROP TABLE IF EXISTS `Categories`;
DROP TABLE IF EXISTS `Users`;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `Users` (
  `user_ID` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pass_hash` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`user_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Categories` (
  `category_ID` binary(16) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`category_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Problems` (
  `problem_ID` bigint NOT NULL,
  `category_ID` int DEFAULT NULL,
  `examples` text,
  `sampleExpectedOutput` varchar(255) DEFAULT NULL,
  `methodName` varchar(255) DEFAULT NULL,
  `parameterType` varchar(255) DEFAULT NULL,
  `difficulty` enum('EASY','HARD','MEDIUM') NOT NULL,
  `prompt` text,
  `returnType` varchar(255) DEFAULT NULL,
  `sampleTestCase` text,
  `starterCode` text,
  `pTitle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`problem_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Proficiencies` (
  `category_ID` int NOT NULL,
  `user_ID` varchar(255) NOT NULL,
  `proficiency` int NOT NULL,
  PRIMARY KEY (`category_ID`,`user_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Test_Cases` (
  `testId` varchar(255) NOT NULL,
  `expected_output` varchar(255) DEFAULT NULL,
  `input_args` varchar(255) DEFAULT NULL,
  `is_hidden` int DEFAULT NULL,
  `problem_ID` int DEFAULT NULL,
  PRIMARY KEY (`testId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Submissions` (
  `submission_ID` varchar(255) NOT NULL,
  `answer` text DEFAULT NULL,
  `rating` enum('GREEN','RED','YELLOW') DEFAULT NULL,
  `submitted_at` datetime(6) NOT NULL,
  `problem_ID` bigint DEFAULT NULL,
  `user_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`submission_ID`),
  KEY `FKj9q1vqurnvydghpbcs1ousalb` (`problem_ID`),
  KEY `FK1cynyo64rcxw3e2orx8g5p3yq` (`user_ID`),
  CONSTRAINT `FK1cynyo64rcxw3e2orx8g5p3yq`
    FOREIGN KEY (`user_ID`) REFERENCES `Users` (`user_ID`),
  CONSTRAINT `FKj9q1vqurnvydghpbcs1ousalb`
    FOREIGN KEY (`problem_ID`) REFERENCES `Problems` (`problem_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
