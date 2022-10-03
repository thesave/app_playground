/*
 * MIT License
 * Copyright (c) 2022 Saverio Giallorenzo <saverio.giallorenzo@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package reachability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
public class ReachAffinity {
	public static void main( String[] args ) throws IOException {
		if ( args.length < 1 ){
			System.out.println( "Provide a path to a nodelist file as first argument");
			System.exit( 1 );
		}
		String nodeList = Files.readString( Path.of( args[ 0 ] ) );
		Graph g = Graph.parse( nodeList );
		if ( args.length > 1 && args[1].equals( "--debug" ) ){
			System.out.println( "Parsed: " + g );
		}
		canReach( g, 10, true );
		System.exit( 0 );
	}

	static boolean canReach( Graph g, int k, boolean topCall ) {
		int total_occupation = g.root + g.children.stream().reduce( 0, ( a, c ) -> a + c.root, Integer::sum );
		if ( total_occupation > k )
			return false;

		if ( g.children.isEmpty() )
			return true;

		Set< Graph[] > permutations = getPermutations( g.children.size(), g.children.toArray( new Graph[ 0 ] ) );

		for ( Graph[] permutation : permutations ){
			if( checkPermutation( permutation, 0, k ) ) {
				if( topCall )
					System.out.println( "Found possible solution: " + Arrays.toString( permutation ) );
				return true;
			}
		}
		return false;
	}

	static boolean canReach( Graph g, int k ){
		return canReach( g, k, false );
	}

	static boolean checkPermutation( Graph[] g, int i, int k ){
		if ( g.length > i && canReach( g[ i ], k ) ) {
//			System.out.println( "OK permutation " + Arrays.toString( g ) + " i: " + i + " k: " + k );
			if( g.length > i+1 ){
				return checkPermutation( g, i+1, k - g[ i ].root );
			} else {
				return true;
			}
		}
		else {
//			System.out.println( "Wrong permutation " + Arrays.toString( g ) + " i: " + i + " k: " + k );
//			System.out.println( "g.length: " + g.length + ", i:" + i +
//							" canReach:" + ( g.length > i ? canReach( g[ i ], k ) : "false" ) );
			return false;
		}
	}

	public static < T >  Set< T[] > getPermutations( int n, T[] a ) {
		T tmp;
		Set< T[] > s = new HashSet<>();
		if ( n == 1 ) {
			s.add( Arrays.copyOf( a, a.length ) );
		} else {
			for ( int i = 0; i < ( n - 1 ); i++ ) {
				s.addAll( getPermutations( n - 1, a ) );
				if ( n % 2 == 0 ) {
					tmp = a[ i ];
					a[ i ] = a[ n - 1 ];
				} else {
					tmp = a[ 0 ];
					a[ 0 ] = a[ n - 1 ];
				}
				a[ n - 1 ] = tmp;
			}
			s.addAll( getPermutations( n - 1, a ) );
		}
		return s;
	}

}
