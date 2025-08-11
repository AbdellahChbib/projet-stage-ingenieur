#!/bin/bash

echo "=== 🚀 Test des APIs d'authentification ==="
echo

# Couleurs pour l'affichage
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8080/api"

echo -e "${BLUE}1. Test de connexion (LOGIN) avec Admin${NC}"
echo "POST $BASE_URL/auth/login"
echo

response=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@azbane.com",
    "password": "admin123"
  }')

http_code=$(echo "$response" | grep "HTTP_CODE:" | cut -d':' -f2)
body=$(echo "$response" | sed '/HTTP_CODE:/d')

echo "Code de réponse: $http_code"
echo "Réponse:"
echo "$body" | jq . 2>/dev/null || echo "$body"
echo

if [ "$http_code" = "200" ]; then
    # Extraire le token d'accès
    token=$(echo "$body" | jq -r '.access_token' 2>/dev/null)
    echo -e "${GREEN}✅ Connexion réussie!${NC}"
    echo "Token: $token"
    echo
    
    echo -e "${BLUE}2. Test d'accès à un endpoint protégé${NC}"
    echo "GET $BASE_URL/user/profile avec token JWT"
    echo
    
    profile_response=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X GET "$BASE_URL/user/profile" \
      -H "Authorization: Bearer $token" \
      -H "Content-Type: application/json")
    
    profile_code=$(echo "$profile_response" | grep "HTTP_CODE:" | cut -d':' -f2)
    profile_body=$(echo "$profile_response" | sed '/HTTP_CODE:/d')
    
    echo "Code de réponse: $profile_code"
    echo "Réponse:"
    echo "$profile_body" | jq . 2>/dev/null || echo "$profile_body"
    echo
    
    if [ "$profile_code" = "200" ]; then
        echo -e "${GREEN}✅ Accès au profil réussi!${NC}"
    else
        echo -e "${RED}❌ Échec d'accès au profil${NC}"
    fi
    
else
    echo -e "${RED}❌ Échec de connexion${NC}"
fi

echo -e "${BLUE}3. Test d'enregistrement d'un nouvel utilisateur${NC}"
echo "POST $BASE_URL/auth/register"
echo

register_response=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "email": "test.user@azbane.com",
    "password": "testpass123",
    "role": "USER",
    "department": "IT",
    "position": "Developer"
  }')

register_code=$(echo "$register_response" | grep "HTTP_CODE:" | cut -d':' -f2)
register_body=$(echo "$register_response" | sed '/HTTP_CODE:/d')

echo "Code de réponse: $register_code"
echo "Réponse:"
echo "$register_body" | jq . 2>/dev/null || echo "$register_body"
echo

if [ "$register_code" = "200" ] || [ "$register_code" = "201" ]; then
    echo -e "${GREEN}✅ Enregistrement réussi!${NC}"
else
    echo -e "${RED}❌ Échec d'enregistrement${NC}"
fi

echo -e "${BLUE}4. Test de connexion avec mauvaises informations${NC}"
echo "POST $BASE_URL/auth/login (mot de passe incorrect)"
echo

bad_login_response=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@azbane.com",
    "password": "wrongpassword"
  }')

bad_login_code=$(echo "$bad_login_response" | grep "HTTP_CODE:" | cut -d':' -f2)
bad_login_body=$(echo "$bad_login_response" | sed '/HTTP_CODE:/d')

echo "Code de réponse: $bad_login_code"
echo "Réponse:"
echo "$bad_login_body" | jq . 2>/dev/null || echo "$bad_login_body"
echo

if [ "$bad_login_code" = "401" ] || [ "$bad_login_code" = "403" ]; then
    echo -e "${GREEN}✅ Rejet correct des mauvaises informations${NC}"
else
    echo -e "${RED}❌ Problème de sécurité détecté${NC}"
fi

echo
echo "=== 📊 Résumé des tests ==="
echo "- Connexion admin: Code $http_code"
echo "- Enregistrement: Code $register_code"  
echo "- Mauvaise connexion: Code $bad_login_code"
echo
