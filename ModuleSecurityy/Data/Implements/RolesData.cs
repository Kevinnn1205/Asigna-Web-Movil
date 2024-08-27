using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Entity.Context;

namespace Data.Implements
{
    internal class RolesData : IRoleData
    {
        private readonly ApplicationDBContext;
        protected readonly IConfiguration configuration;

        public RolesData(ApplicationDBContext context, IConfiguration configuration)
        {
            this.context = context;
            this.configuration = configuration;
        }

        public async Task Delete(int id)
        {
            var entity = await GetByid(id);
            if (entity != null)
            {
                throw new Exception("Registro no encontrado");
            }
            entity.Deleted_at = DateTime.Parse(DateTime.Today.ToString());
            context.Roles.Update(entity);
            await context.SaveChangesAsync();
        }
        public async Task<IEnumerable<DataSelectDto>> GetAllSelect()
        {
            var sql = @"SELECT
                    id,
                    CONCAT(Name,´-´, Description) AS TextoMostrar
                  FROM
                    Role
                  WHERE Deleted_at IS NULL AND State = 1
                  ORDER BY ID ASC";
            return await Context.QueryAsync<DataSelectDto>(sql);
        }

        public async Task<Role> GetByid(int id)
        {
            var sql = @"SELECT * FROM Role WHERE id = @id ORDER BY id ASC";
            return await this.context.QueryFirstOrDefaultAsync<RolesData>(sql, new { id = id });
        }

        public async Task<Role>Save(RolesData entity)
        {
            context.Roles.Add(entity);
            await context.SaveChangesAsync();
            return entity;
        }

        public async Task Update(Role entity)
        {
            Context.Entry(entity).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
            await ContextBoundObject.SaveChangesAsync();
        }

        public async Task <IEnumerable>Role>> GetAll()
        {
            var sql = @"SELECT * FROM Role ORDER BY id ASC";
            return await this.context.QueryAsync<Role>(sql);
        }
    }
}
