[string]$sqlite_library_path = "Z:\Microsoft Visual Studio 12.0\Common7\IDE\PublicAssemblies\sqlite3.dll"
[string]$db_data_source = "C:\Users\$env:USERNAME\AppData\Local\Google\Chrome\User Data\Default\Cookies"
[string]$db_query = "SELECT * FROM cookies WHERE name='cookiename' AND host_key='servername'"

[void][System.Reflection.Assembly]::LoadFrom($sqlite_library_path)

$db_dataset = New-Object System.Data.DataSet

$db_data_adapter = New-Object System.Data.SQLite.SQLiteDataAdapter($db_query,"Data Source=$db_data_source")
[void]$db_data_adapter.Fill($db_dataset)
$db_dataset.Tables[0].encrypted_value

# Load System.Security assembly
Add-Type -AssemblyName System.Security

# Decrypt cookie
$ByteArr = [System.Security.Cryptography.ProtectedData]::Unprotect(
                $db_dataset.Tables[0].encrypted_value,
                $null,
                [System.Security.Cryptography.DataProtectionScope]::CurrentUser
            )

# Convert to string
$DecryptedCookie = [System.Text.Encoding]::ASCII.GetString($ByteArr)

Write-Host $DecryptedCookie