from django.core.management.base import BaseCommand
from django_faculty_DB.models import User


class Command(BaseCommand):
    help = 'Delete all users except the first 500'

    def handle(self, *args, **options):
        users = User.objects.order_by('id')[500:]
        count = users.count()
        for user in users:
            user.delete()
        self.stdout.write(self.style.SUCCESS(f'Successfully deleted {count} users'))
