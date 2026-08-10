-- Minimal local demo data for SmartPrep.
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
    'Return true when any integer appears more than once.',
    'boolean',
    'nums = [1,2,3,1]',
    'import java.util.*; public class Solution { public boolean containsDuplicate(int[] nums) { Set<Integer> seen=new HashSet<>(); for(int n:nums) if(!seen.add(n)) return true; return false; } }',
    'Contains Duplicate'
  ),
  (
    201,
    2,
    '"racecar"',
    'true',
    'isPalindrome',
    'String',
    'EASY',
    'Return true when the string reads the same forward and backward.',
    'boolean',
    's = "racecar"',
    'public class Solution { public boolean isPalindrome(String s) { int l=0,r=s.length()-1; while(l<r) if(s.charAt(l++)!=s.charAt(r--)) return false; return true; } }',
    'Valid Palindrome'
  ),
  (
    301,
    3,
    '[[2,7,11,15],9]',
    '[0,1]',
    'twoSum',
    'int[],int',
    'EASY',
    'Return the indices of two values whose sum equals the target.',
    'int[]',
    'nums = [2,7,11,15], target = 9',
    'import java.util.*; public class Solution { public int[] twoSum(int[] a,int t) { Map<Integer,Integer> m=new HashMap<>(); for(int i=0;i<a.length;i++){ if(m.containsKey(t-a[i])) return new int[]{m.get(t-a[i]),i}; m.put(a[i],i);} return new int[0]; } }',
    'Two Sum'
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
  ('201-1', 'true', '"racecar"', 0, 201),
  ('201-2', 'false', '"smartprep"', 1, 201),
  ('301-1', '[0,1]', '[[2,7,11,15],9]', 0, 301),
  ('301-2', '[1,2]', '[[3,2,4],6]', 1, 301);
