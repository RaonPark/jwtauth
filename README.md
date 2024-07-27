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
<img width="490" alt="image" src="https://github.com/user-attachments/assets/08a4bc74-cc20-4a4c-8936-504d06f0b0de"><br/>

### GET: /member/login <br/>
?loginId=raonpark&password=12345678<br/>
<img width="556" alt="image" src="https://github.com/user-attachments/assets/b4b26080-090e-4cbb-ba99-cbf5190173aa"><br/>

### GET: /member/jwt

<img width="596" alt="image" src="https://github.com/user-attachments/assets/fdcb0f7a-de7a-4999-938f-b38a286af090"><br/>
- 403 Forbidden인 이유는 SecurityConfig에서 permitAll을 해주지 않아서 그렇다. 테스트용 API이다.<br/>

### GET: /member/{id}
@PathVariable id: String <br/>
return type:
```json
{
  "id": UUID,
  "loginId": String,
  "name": String,
  "authority": String
}
```
<img width="587" alt="image" src="https://github.com/user-attachments/assets/8738ad70-245e-46ab-819c-05715a577fb4"><br/>

### POST: /guitar/newGuitar
```json
{
  "model": "Taylor 314CE",
  "price": 3300,
  "companyName": "Taylor Guitars",
  "isUsed": false
}
```
<img width="749" alt="image" src="https://github.com/user-attachments/assets/55c1351d-849b-4cef-86ac-727b0719ff3c"><br/>
<img width="330" alt="image" src="https://github.com/user-attachments/assets/47a3b9ce-5d12-4d0f-8407-cb8611a00aee"><br/>

### POST: /guitar/newGuitarCompany
```json
{
  "companyName": "Taylor Guitars",
  "established": "1974-06-01T12:00:00"
}
```
<img width="755" alt="image" src="https://github.com/user-attachments/assets/3a825f41-261d-4a4d-8e7b-7b9fc42dbc08"><br/>
<img width="271" alt="image" src="https://github.com/user-attachments/assets/c3180027-a4e9-44c9-b781-02ea2b2459e4"><br/>
- Postman을 사용한 LocalDateTime 직렬화를 할 때 제대로 되지 않아서 (KSerialize라는 클래스를 찾지 못하는 오류가 발생함) kotlinx-serialization-json 패키지와 kotlinx-datetime 패키지를 추가하여 해결할 수 있었다.<br/>




