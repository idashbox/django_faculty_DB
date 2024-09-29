from faker import Faker
import random
from django.core.management.base import BaseCommand
from django_faculty_DB.models import User

fake = Faker()

class Command(BaseCommand):
    help = 'Generate test data for the database'

    def handle(self, *args, **kwargs):
        self.create_users(1000)

    def create_users(self, num):
        users = []
        used_names = set()  # Для уникальных имен
        used_emails = set()  # Для уникальных email

        for _ in range(num):
            # Генерация уникального имени
            name = fake.name()
            while name in used_names or User.objects.filter(name=name).exists():
                name = fake.name() + str(random.randint(1, 10000))  # Уникальный суффикс
            used_names.add(name)

            # Генерация уникального email
            email = fake.unique.email()
            while email in used_emails or User.objects.filter(email=email).exists():
                email = fake.email() + str(random.randint(1, 10000))  # Уникальный суффикс
            used_emails.add(email)

            # Создание пользователя
            user = User(
                name=name,
                email=email,
                login=fake.user_name(),
                password=fake.password(),
                surname=fake.last_name(),
                middle_name=fake.first_name(),
                birthday=fake.date_of_birth(),
                sex=random.choice(['Male', 'Female']),
                role=random.choice(['student', 'teacher'])
            )
            users.append(user)

        # Вставка в базу данных
        User.objects.bulk_create(users)
        self.stdout.write(self.style.SUCCESS(f'Created {num} users'))
