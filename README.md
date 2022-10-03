# APP playground

## reachability
Premutation-based algorithm to check reachability of a function dependency graph with affinity constraints.
Download the compiled version from the [Release](https://github.com/thesave/app_playground/releases) menu.

Command:
`java -jar reachability.jar path/to/nodelist.txt [--debug]`

The nodelist format assumes no comments and that the first line corresponds to the root of the DAG.

Nodelist format:
```
nodeName weight dep1 dep2 dep3
dep1 weight dep3 dep4 
...```
