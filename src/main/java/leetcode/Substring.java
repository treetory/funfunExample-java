package leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 *  https://leetcode.com/problems/longest-substring-without-repeating-characters/
 *
 *  3. Longest Substring Without Repeating Characters
 *
 *  Given a string, find the length of the longest substring without repeating characters.
 *
 * Example 1:
 *
 * Input: "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 * Example 2:
 *
 * Input: "bbbbb"
 * Output: 1
 * Explanation: The answer is "b", with the length of 1.
 * Example 3:
 *
 * Input: "pwwkew"
 * Output: 3
 * Explanation: The answer is "wke", with the length of 3.
 *              Note that the answer must be a substring, "pwke" is a subsequence and not a substring.
 *
 *
 *
 * ---> Memoization VS. Dynamic Programming
 * ---> 함수형으로 풀기 위해선 Memoization 을 사용해야 한다.
 */
public class Substring {

    private static final Logger LOG = LoggerFactory.getLogger(Substring.class);

    public static int lengthOfLongestSubstring(String s) {
        Hashtable hash=new Hashtable();
        int length=s.length();
        int max=0;
        int availablefrom=0;
        for(int i=0;i<length;i++){
            if(hash.containsKey(s.charAt(i))){
                int last=(Integer) hash.get(s.charAt(i));
                availablefrom=Math.max(availablefrom, last+1);
            }
            max=Math.max(max, i-availablefrom+1);
            LOG.debug("S : {}, boolean : {}, max : {}, availablefrom : {}, i : {}", s.charAt(i), hash.containsKey(s.charAt(i)), max, availablefrom, i);
            hash.put(s.charAt(i),i);
        }
        return max;

    }

    public static void main(String[] args) {
        LOG.debug("{}", lengthOfLongestSubstring("abcabcbb"));
        LOG.debug("{}", lengthOfLongestSubstring("bbbbb"));
        LOG.debug("{}", lengthOfLongestSubstring("pwwkew"));
        LOG.debug("{}", lengthOfLongestSubstring("dvdf"));
    }

}