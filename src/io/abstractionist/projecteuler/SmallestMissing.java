package io.abstractionist.projecteuler;

import java.util.Arrays;

public class SmallestMissing {
  public static void main(String[] args) {

    int[] problem = {-1, -3};

    System.out.println(String.format("Smallest missing element is: %d", solution(problem)));

  }

  public static int solution(int[] A) {
    // result should be greater than 0
    // if there is no missing integer between elements,
    // returns last element + 1
    // if all elements are less than 0, return 1

    int smallest = 0;
    boolean found = false;
    Arrays.sort(A);
    for (int i = 0; i < A.length - 1; i++) {
      if (A[i + 1] - A[i] > 1) {
        smallest = A[i] + 1;
        found = true;
        break;
      }
    }

    if (!found) {
      smallest = A[A.length - 1] + 1;
    }

    if (smallest < 0)
      return 1;

    return smallest;
  }
}
