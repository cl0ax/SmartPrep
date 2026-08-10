-- Presentable local demo data for SmartPrep.
-- Users and proficiencies are created through the application signup flow.

SET NAMES utf8mb4;

INSERT INTO `Categories` (`category_ID`, `name`) VALUES
  (UUID_TO_BIN('00000000-0000-0000-0000-000000000001'), 'Arrays & Strings'),
  (UUID_TO_BIN('00000000-0000-0000-0000-000000000002'), 'Two Pointers'),
  (UUID_TO_BIN('00000000-0000-0000-0000-000000000003'), 'Hash Maps');

INSERT INTO `Problems` (
  `problem_ID`,
  `category_ID`,
  `examples`,
  `sampleExpectedOutput`,
  `methodName`,
  `parameterType`,
  `difficulty`,
  `prompt`,
  `returnType`,
  `sampleTestCase`,
  `starterCode`,
  `pTitle`
) VALUES
  (
    101,
    1,
    '[1,2,3,1]',
    'true',
    'containsDuplicate',
    'int[]',
    'EASY',
    'Given an integer array nums, return true if any value appears at least twice. Return false if every element is distinct.',
    'boolean',
    'Input: nums = [1,2,3,1]
Output: true
Explanation: The value 1 appears at indices 0 and 3.',
    'import java.util.*;

public class Solution {
    public boolean containsDuplicate(int[] nums) {
        // your code here
        return false;
    }
}',
    'Contains Duplicate'
  ),
  (
    102,
    1,
    '"abcabcbb"',
    '3',
    'lengthOfLongestSubstring',
    'String',
    'MEDIUM',
    'Given a string s, return the length of the longest substring that contains no repeated characters.',
    'int',
    'Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with a length of 3.',
    'import java.util.*;

public class Solution {
    public int lengthOfLongestSubstring(String s) {
        // your code here
        return 0;
    }
}',
    'Longest Substring Without Repeating Characters'
  ),
  (
    201,
    2,
    '"A man, a plan, a canal: Panama"',
    'true',
    'isPalindrome',
    'String',
    'EASY',
    'After converting uppercase letters to lowercase and removing non-alphanumeric characters, return true if s reads the same forward and backward.',
    'boolean',
    'Input: s = "A man, a plan, a canal: Panama"
Output: true
Explanation: The normalized string is "amanaplanacanalpanama".',
    'public class Solution {
    public boolean isPalindrome(String s) {
        // your code here
        return false;
    }
}',
    'Valid Palindrome'
  ),
  (
    202,
    2,
    '[1,8,6,2,5,4,8,3,7]',
    '49',
    'maxArea',
    'int[]',
    'MEDIUM',
    'Given heights of vertical lines, choose two lines that hold the most water with the x-axis. Return the maximum area.',
    'int',
    'Input: height = [1,8,6,2,5,4,8,3,7]
Output: 49
Explanation: Lines at indices 1 and 8 form an area of 7 * 7.',
    'public class Solution {
    public int maxArea(int[] height) {
        // your code here
        return 0;
    }
}',
    'Container With Most Water'
  ),
  (
    203,
    2,
    '[0,1,0,2,1,0,1,3,2,1,2,1]',
    '6',
    'trap',
    'int[]',
    'HARD',
    'Given non-negative bar heights where each bar has width 1, return how many units of rain water are trapped after raining.',
    'int',
    'Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
Output: 6',
    'public class Solution {
    public int trap(int[] height) {
        // your code here
        return 0;
    }
}',
    'Trapping Rain Water'
  ),
  (
    301,
    3,
    '[[2,7,11,15],9]',
    '[0,1]',
    'twoSum',
    'int[],int',
    'EASY',
    'Given an integer array nums and an integer target, return the indices of the two numbers that add up to target. Exactly one answer exists.',
    'int[]',
    'Input: nums = [2,7,11,15], target = 9
Output: [0,1]
Explanation: nums[0] + nums[1] equals 9.',
    'import java.util.*;

public class Solution {
    public int[] twoSum(int[] nums, int target) {
        // your code here
        return new int[0];
    }
}',
    'Two Sum'
  ),
  (
    302,
    3,
    '[100,4,200,1,3,2]',
    '4',
    'longestConsecutive',
    'int[]',
    'MEDIUM',
    'Given an unsorted integer array nums, return the length of the longest sequence of consecutive values. The algorithm should run in O(n) time.',
    'int',
    'Input: nums = [100,4,200,1,3,2]
Output: 4
Explanation: The longest consecutive sequence is [1,2,3,4].',
    'import java.util.*;

public class Solution {
    public int longestConsecutive(int[] nums) {
        // your code here
        return 0;
    }
}',
    'Longest Consecutive Sequence'
  ),
  (
    303,
    3,
    '["ADOBECODEBANC","ABC"]',
    '"BANC"',
    'minWindow',
    'String,String',
    'HARD',
    'Given strings s and t, return the shortest substring of s that contains every character in t, including duplicate characters. Return an empty string if none exists.',
    'String',
    'Input: s = "ADOBECODEBANC", t = "ABC"
Output: "BANC"
Explanation: "BANC" is the shortest window containing A, B, and C.',
    'import java.util.*;

public class Solution {
    public String minWindow(String s, String t) {
        // your code here
        return "";
    }
}',
    'Minimum Window Substring'
  );

INSERT INTO `Test_Cases` (
  `testId`,
  `expected_output`,
  `input_args`,
  `is_hidden`,
  `problem_ID`
) VALUES
  ('101-1', 'true', '[1,2,3,1]', 0, 101),
  ('101-2', 'false', '[1,2,3,4]', 1, 101),
  ('101-3', 'true', '[1,1]', 1, 101),
  ('102-1', '3', '"abcabcbb"', 0, 102),
  ('102-2', '1', '"bbbbb"', 1, 102),
  ('102-3', '3', '"pwwkew"', 1, 102),
  ('201-1', 'true', '"A man, a plan, a canal: Panama"', 0, 201),
  ('201-2', 'false', '"race a car"', 1, 201),
  ('201-3', 'true', '" "', 1, 201),
  ('202-1', '49', '[1,8,6,2,5,4,8,3,7]', 0, 202),
  ('202-2', '1', '[1,1]', 1, 202),
  ('202-3', '16', '[4,3,2,1,4]', 1, 202),
  ('203-1', '6', '[0,1,0,2,1,0,1,3,2,1,2,1]', 0, 203),
  ('203-2', '9', '[4,2,0,3,2,5]', 1, 203),
  ('203-3', '0', '[3,2,1]', 1, 203),
  ('301-1', '[0,1]', '[[2,7,11,15],9]', 0, 301),
  ('301-2', '[1,2]', '[[3,2,4],6]', 1, 301),
  ('301-3', '[0,1]', '[[3,3],6]', 1, 301),
  ('302-1', '4', '[100,4,200,1,3,2]', 0, 302),
  ('302-2', '9', '[0,3,7,2,5,8,4,6,0,1]', 1, 302),
  ('302-3', '1', '[10]', 1, 302),
  ('303-1', '"BANC"', '["ADOBECODEBANC","ABC"]', 0, 303),
  ('303-2', '"a"', '["a","a"]', 1, 303),
  ('303-3', '""', '["a","aa"]', 1, 303);
