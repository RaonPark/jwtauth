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

## 에러 해결 방법
### 1. KoTest에서 No transaction in context 에러
```kotlin
SchemaUtils.create(MemberTable, BoardTable)

given("게시판에 글을 쓸 유저 한 명이 주어진다.") {
val user = transaction {
    MemberTable.insertAndGetId {
        it[loginId] = "raonpark"
        it[name] = "박수민"
        it[password] = "1234"
        it[authority] = "USER"
    }
}

`when`("게시판에 raonpark 아이디를 가진 유저가 글을 쓴다.") {
    transaction {
        BoardTable.insert {
            it[title] = "테일러 314CE 사실 분?"
            it[content] = "테일러 314CE 2년 안됐는데 사실 분 구합니다."
            it[image] = "asdfawerw"
            it[username] = user
            it[published] = CurrentDateTime
            it[type] = TypeOfBoard.TRADING
        }
    }

    then("게시판에 새로운 글 하나가 올라왔다.") {
        transaction { BoardTable.selectAll().count() } shouldBeGreaterThan 0
    }
}
```
이 경우에서 Database.connect()는 괜찮지만 SchemaUtils.create()는 transaction {} 블록에 넣어야 no transaction in context 문제가 해결된다.

<img width="528" alt="스크린샷 2024-10-03 오후 4 59 05" src="https://github.com/user-attachments/assets/e5f56510-e9b1-4870-be92-8efea4cc61a0"><br/>

### 2. IllegalStateException : No target provided for update
- 해결 못하고 mock을 걷어내는 것으로 해결했다..
- 굳이 H2에다가 Exposed를 사용하는데 mock까지 사용할 필요는 없는것 같다.
- mockk에 대해서 조금 공부할 필요는 있을 것 같다.

