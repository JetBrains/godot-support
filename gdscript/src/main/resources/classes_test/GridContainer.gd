extends Container
#brief Grid container used to arrange Control-derived children in a grid like layout.
#desc GridContainer will arrange its Control-derived children in a grid like structure, the grid columns are specified using the [member columns] property and the number of rows will be equal to the number of children in the container divided by the number of columns. For example, if the container has 5 children, and 2 columns, there will be 3 rows in the container.
#desc Notice that grid layout will preserve the columns and rows for every size of the container, and that empty columns will be expanded automatically.
#desc [b]Note:[/b] GridContainer only works with child nodes inheriting from Control. It won't rearrange child nodes inheriting from Node2D.
class_name GridContainer


#desc The number of columns in the [GridContainer]. If modified, [GridContainer] reorders its Control-derived children to accommodate the new layout.
var columns: int;




