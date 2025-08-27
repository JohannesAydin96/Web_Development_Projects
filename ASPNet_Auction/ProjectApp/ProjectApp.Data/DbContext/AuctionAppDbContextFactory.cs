using Microsoft.EntityFrameworkCore.Design;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectApp.Data.DbContext
{
    public class AuctionAppDbContextFactory : IDesignTimeDbContextFactory<AuctionAppDbContext>
    {
        //public AuctionAppDbContext CreateDbContext(string[] args)
        //{
        //    var configuration = new ConfigurationBuilder()
        //        .SetBasePath(Directory.GetCurrentDirectory())
        //        .AddJsonFile("appsettings.json")
        //        .Build();

        //    var optionsBuilder = new DbContextOptionsBuilder<AuctionAppDbContext>();

        //    // Configure MySQL connection here
        //    optionsBuilder.UseMySQL(configuration.GetConnectionString("AuctionAppConnection"));

        //    return new AuctionAppDbContext(optionsBuilder.Options);
        //}

        public AuctionAppDbContext CreateDbContext(string[] args)
        {
            var configuration = GetConfiguration();

            var optionsBuilder = new DbContextOptionsBuilder<AuctionAppDbContext>();
            var connectionString = configuration.GetConnectionString("DefaultConnection");

            // Use MySQL
            optionsBuilder.UseMySql(connectionString, new MySqlServerVersion(new Version(8, 0, 32))); // Replace with your MySQL version

            return new AuctionAppDbContext(optionsBuilder.Options);
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
