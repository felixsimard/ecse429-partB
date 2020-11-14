package ecse429.storytesting.model;

class Project(var id: Int,
           var title: String,
           var completed: Boolean,
           var active: Boolean,
           var description: String,
           val tasks: List<Id>)
