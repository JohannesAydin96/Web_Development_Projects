using Microsoft.EntityFrameworkCore;
using ProjectApp.Data.DbContext;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectApp.Business.Services
{
    //public class UserService
    //{

    //    private readonly CustomIdentityDbContext _context;

    //    public UserService(CustomIdentityDbContext context)
    //    {
    //        _context = context;
    //    }

    //    public async Task<string> GetUsernameFromIdentityServer(Guid userId)
    //    {
    //        var user = await _context.AspNetUsers
    //            .Where(u => u.Id == userId)
    //            .Select(u => u.UserName)
    //            .FirstOrDefaultAsync();

    //        if (user == null)
    //            throw new Exception("User not found.");

    //        return user;
    //    }
    //}
}
