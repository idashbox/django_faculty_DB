from faker import Faker
import random
from django.core.management.base import BaseCommand
from myapp.models import User, UserToGroup, Teacher, Department, Group

fake = Faker()


class Command(BaseCommand):
    help = 'Generate test data for UserToGroup, Teacher and Department models'

    def handle(self, *args, **kwargs):
        teachers = self.create_teachers(10)
        self.create_departments(teachers[:3])
        self.create_user_to_groups(10)

    def create_teachers(self, num):
        users = User.objects.filter(role='teacher')[:num]
        teachers = []

        for user in users:
            teacher = Teacher(
                user=user,
                year_of_start_of_work=str(random.randint(2000, 2023))
            )
            teachers.append(teacher)

        Teacher.objects.bulk_create(teachers)
        self.stdout.write(self.style.SUCCESS(f'Created {num} teachers'))
        return Teacher.objects.all()[:num]

    def create_departments(self, teachers):
        department_titles = ['ПИВиС', 'ТОиЗИ', 'ИТУ']
        departments = []

        for i, title in enumerate(department_titles):
            department = Department(
                title=title,
                head_of_department=teachers[i]
            )
            departments.append(department)

        Department.objects.bulk_create(departments)
        self.stdout.write(self.style.SUCCESS('Created 3 departments with heads of department'))

    def create_user_to_groups(self, num):
        users = User.objects.filter(role='student')[:num]
        groups = Group.objects.all()[:num]
        user_to_groups = []

        for user, group in zip(users, groups):
            user_to_group = UserToGroup(
                user=user,
                group=group
            )
            user_to_groups.append(user_to_group)

        UserToGroup.objects.bulk_create(user_to_groups)
        self.stdout.write(self.style.SUCCESS(f'Created {num} UserToGroup records'))
