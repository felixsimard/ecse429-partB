package ecse429.storytesting.Model

class Todo(var id: Int,
           var title: String,
           var doneStatus: Boolean,
           var description: String,
           val tasksof: List<Id>)