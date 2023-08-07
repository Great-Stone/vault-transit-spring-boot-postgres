볼트 서버를 설정하고 Spring Boot에서 볼트를 사용하여 구성정보를 읽는 과정을 설명한다.

앱을 위한 볼트 구성을 위해 다음과 같이 볼트를 실행한다.

```bash
$ vault server -dev -dev-root-token-id=root -log-level=trace

...
You may need to set the following environment variables:

    $ export VAULT_ADDR='http://127.0.0.1:8200'

The unseal key and root token are displayed below in case you want to
seal/unseal the Vault or re-authenticate.

Unseal Key: UTZ7HoZCu8dtWa/eSMKcwq1klhC/qFoDxHXmhRn4qnE=
Root Token: root
```

`root` 토큰은 구성관리 관리자의 권한으로 가정한다.

```bash
$ export VAULT_ADDR='http://127.0.0.1:8200'
$ vault login
Token (will be hidden): root

Success! You are now authenticated. The token information displayed below
is already stored in the token helper. You do NOT need to run "vault login"
again. Future Vault requests will automatically use this token.

Key                  Value
---                  -----
token                root
token_accessor       w5LvrjTvDDcfjPHrnOj6ib7E
token_duration       ∞
token_renewable      false
token_policies       ["root"]
identity_policies    []
policies             ["root"]
```

Spring Boot 앱에서 사용할 Transit를 활성화 한다.

```bash
$ vault secrets enable -path=transit transit

Success! Enabled the transit secrets engine at: transit/
```



