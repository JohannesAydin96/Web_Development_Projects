using Microsoft.EntityFrameworkCore.Design;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;

namespace ProjectApp.Data.DbContext
{
    //public class CustomIdentityDbContextFactory : IDesignTimeDbContextFactory<CustomIdentityDbContext>
    //{
    //    public CustomIdentityDbContext CreateDbContext(string[] args)
    //    {
    //        var optionsBuilder = new DbContextOptionsBuilder<CustomIdentityDbContext>();
    //        optionsBuilder.UseSqlServer("Server=DESKTOP-S00ESS3;Database=IdentityDB2;Trusted_Connection=True;TrustServerCertificate=True;");

    //        return new CustomIdentityDbContext(optionsBuilder.Options);
    //    }
    //}


    public class CustomIdentityDbContextFactory : IDesignTimeDbContextFactory<CustomIdentityDbContext>
    {
        public CustomIdentityDbContext CreateDbContext(string[] args)
        {
            var configuration = GetConfiguration();

            var optionsBuilder = new DbContextOptionsBuilder<CustomIdentityDbContext>();
            var connectionString = configuration.GetConnectionString("IdentityConnection");

            // Use MySQL
            optionsBuilder.UseMySql(connectionString, new MySqlServerVersion(new Version(8, 0, 32))); // Replace with your MySQL version

            return new CustomIdentityDbContext(optionsBuilder.Options);
        }

        private IConfiguration GetConfiguration()
        {
            // Using Directory.GetCurrentDirectory() to get the base path manually
            var basePath = Directory.GetCurrentDirectory();

            // Now build configuration from the appsettings.json file
            var configuration = new ConfigurationBuilder()
                .SetBasePath(basePath) // Optional if base path is automatically handled
                .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
                .Build();

            return configuration;
        }
    }
}
