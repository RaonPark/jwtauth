## 사용한 기술 스택
1. Kotlin
2. Spring-Boot
3. Exposed
4. Spring-Boot Security

## API
### POST: /member/register
```json
{
  "loginId": "raonpark",
  "password": "12345678",
  "name": "raonpark",
  "role": "user"
}
```
<img width="490" alt="image" src="https://github.com/user-attachments/assets/08a4bc74-cc20-4a4c-8936-504d06f0b0de">

### GET: /member/login
?loginId=raonpark&password=12345678

<img width="556" alt="image" src="https://github.com/user-attachments/assets/b4b26080-090e-4cbb-ba99-cbf5190173aa">

### GET: /member/jwt

<img width="596" alt="image" src="https://github.com/user-attachments/assets/fdcb0f7a-de7a-4999-938f-b38a286af090">
- 403 Forbidden인 이유는 SecurityConfig에서 permitAll을 해주지 않아서 그렇다. 테스트용 API이다.
