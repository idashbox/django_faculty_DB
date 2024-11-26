from django.contrib import admin
from myapp.models import User, Teacher, Department, Group, DirectionOfStudy, Discipline, UserToGroup

admin.site.register(User)
admin.site.register(Teacher)
admin.site.register(Department)
admin.site.register(Group)
admin.site.register(DirectionOfStudy)
admin.site.register(Discipline)
admin.site.register(UserToGroup)
