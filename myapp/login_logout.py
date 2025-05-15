from django.contrib.auth import authenticate
from rest_framework.authtoken.models import Token
from rest_framework.authentication import TokenAuthentication
from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .models import Teacher, UserToGroup
from .models import User

class LoginView(APIView):
    def post(self, request):
        username = request.data.get('username')
        password = request.data.get('password')
        user = authenticate(username=username, password=password)
        if user:
            token, _ = Token.objects.get_or_create(user=user)
            return Response({'token': token.key}, status=status.HTTP_200_OK)
        return Response({'error': 'Invalid Credentials'}, status=status.HTTP_401_UNAUTHORIZED)


class LogoutView(APIView):
    authentication_classes = [TokenAuthentication]
    permission_classes = [IsAuthenticated]

    def post(self, request):
        request.user.auth_token.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class ProfileView(APIView):
    authentication_classes = [TokenAuthentication]
    permission_classes = [IsAuthenticated]

    def get(self, request):
        user = User.objects.filter(login=request.user.username, surname=request.user.last_name).first()
        print(f"Authenticated as: {user}")

        try:
            if Teacher.objects.filter(user=user).exists():
                role = "teacher"
            elif UserToGroup.objects.filter(user=user).exists():
                role = "usertogroup"
            else:
                role = "non"
        except Exception as e:
            print(f"Ошибка при определении роли: {e}")
            return Response({'error': 'Internal Server Error'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

        try:
            profile_data = {
                'username': user.login,
                'email': user.email,
                'first_name': user.name,
                'last_name': user.surname,
                'role': role
            }
            print("Профиль собран: ", profile_data)
        except Exception as e:
            print(f"Ошибка при сборке данных профиля: {e}")
            return Response({'error': 'Internal Server Error'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

        return Response(profile_data, status=status.HTTP_200_OK)

