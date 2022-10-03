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

import java.util.*;
import java.util.stream.Collectors;
public class Graph {
	final int root;
	final String label;
	final Set< Graph > children;

	public Graph( String label, int root ) {
		this.root = root;
		this.label = label;
		this.children = new HashSet<>();
	}

	public static Graph parse( String nodeList ) {
		Graph main = new Graph( "null", 0 );
		HashMap< String, Graph > map = new HashMap<>();
		List< Map.Entry< Graph, List< String > > > toResolve = new ArrayList<>();
		String[] lines = nodeList.trim().split( "\n" );
		for ( int i = 0; i < lines.length; i++ ) {
			List< String > node = new ArrayList<>( Arrays.stream( lines[ i ].split( "\s+" ) ).toList() );
			Graph g = new Graph( node.remove( 0 ), Integer.parseInt( node.remove( 0 ) ) );
			if( i == 0 ) main = g;
			map.put( g.label, g );
			toResolve.add( new AbstractMap.SimpleEntry<>( g, node ) );
		}
		for ( Map.Entry< Graph, List< String > > pair : toResolve ) {
			for( String dep : pair.getValue() ){
				if( map.containsKey( dep ) ){
					pair.getKey().children.add( map.get( dep ) );
				} else {
					throw new RuntimeException( "Dependency missing for " + pair.getKey().label + ": " + dep );
				}
			}
		}
		return main;
	}

	@Override
	public String toString() {
		return label + "(" + root + ")" + (
						children.size() == 0 ? "" :
										" { " + children.stream().map( Graph::toString ).collect( Collectors.joining( ", " ) ) + " }"
		);
	}

}




