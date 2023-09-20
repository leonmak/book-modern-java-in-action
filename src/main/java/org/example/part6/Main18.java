package org.example.part6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main18 {

    public static void main(String[] args) {
        List<Integer> list = List.of(1, 4, 9);
        List<List<Integer>> subsets = subsets(list);
        System.out.println(subsets);
    }


    /**
     * List<Integer>를 받아서 Integer를 하나씩 뽑아서 List<List<Integer>>에 넣어서 리턴
     * ex. {1, 4, 9} ->  {1, 4, 9}, {1, 4}, {1, 9}, {4, 9}, {1}, {4}, {9}, and {}
     */
    static List<List<Integer>> subsets(List<Integer> list) {
        if (list.isEmpty()) {
            List<List<Integer>> ans = new ArrayList<>();
            ans.add(Collections.emptyList());
            return ans;
        }

        Integer fst = list.get(0); // 첫번째 요소
        List<Integer> rest = list.subList(1, list.size()); // 첫번째 요소를 제외한 나머지 요소들

        List<List<Integer>> subAns = subsets(rest); // 나머지 요소들로 만들 수 있는 모든 부분집합
        List<List<Integer>> subAns2 = insertAll(fst, subAns); // 나머지 요소 부분 집합 + 첫번쨰 요소
        return concat(subAns, subAns2);
    }

    static List<List<Integer>> insertAll(Integer fst, List<List<Integer>> lists) {
        List<List<Integer>> result = new ArrayList<>();

        for (List<Integer> list : lists) {
            List<Integer> copyList = new ArrayList<>();
            copyList.add(fst);
            copyList.addAll(list);
            result.add(copyList);
        }

        return result;
    }

    static List<List<Integer>> concat(List<List<Integer>> a, List<List<Integer>> b) {
        // a.addAll(b);
        // return a;

        // Pure function : argument를 수정하지 않고 새로운 객체를 만들어서 리턴
        List<List<Integer>> r = new ArrayList<>(a);
        r.addAll(b);
        return r;
    }
}
