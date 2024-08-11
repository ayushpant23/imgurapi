// Java code for Stream of(T... values)
// to get a sequential ordered stream whose
// elements are the specified values.
package com.example.demo;

import java.util.*;
import java.util.stream.Stream;

class GFG {

	// Driver code
	public static void main(String[] args)
	{
		// Creating an Stream
       	String[] arr = { "Geeks", "for", "Geeks" };
		Stream stream = Stream.of(arr).distinct();
		
		List<Integer> ll=  new ArrayList<>();
		ll.add(1);
		ll.add(4);
		ll.add(6);
		
		Stream ss= Stream.of(ll);
		int max = ll.stream().max(Integer::compare).get();
		//int max= ss.max(Integer::compare).get();

		// Displaying the sequential ordered stream
		stream.forEach(str -> System.out.print(str + " "));
		
	}
}
