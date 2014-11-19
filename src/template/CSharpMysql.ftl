${adapter}.InsertCommand = new MySqlCommand("insert into ${table} (id<#list list as en>,${en.name}</#list>)values(id<#list list as en>,@${en.name}</#list>)", ${con});
<#list list as en>
${adapter}.InsertCommand.Parameters.Add("@${en.name}", ${en.type}, ${en.typeLen}, "${en.name}");
</#list>


${adapter}.UpdateCommand = new MySqlCommand("update ${table} set id=id<#list list as en>,${en.name}=@${en.name}</#list>  where id=@id", ${con});
${adapter}.UpdateCommand.Parameters.Add("@id", MySqlDbType.Int16, 4, "id");
<#list list as en>
${adapter}.UpdateCommand.Parameters.Add("@${en.name}", ${en.type}, ${en.typeLen}, "${en.name}");
</#list>

${adapter}.DeleteCommand = new MySqlCommand("delete from ${table} where id=@id", ${con});
${adapter}.DeleteCommand.Parameters.Add("@id", MySqlDbType.Int16, 4, "id");